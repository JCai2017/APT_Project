#!/usr/bin/env python

# [START imports]
import os

from google.appengine.api import users
from google.appengine.api import mail
from google.appengine.ext import ndb
from twilio import twiml
from twilio.rest import Client

import jinja2
import webapp2
import logging
import re
import json

class TimeLine(ndb.Model):
	name = ndb.StringProperty(indexed=True)
	ownerEmail = ndb.StringProperty()

class Event(ndb.Model):
	ownerEmail = ndb.StringProperty()
	title = ndb.StringProperty()
	time = ndb.StringProperty()
	timeLine = ndb.KeyProperty(kind=TimeLine)
	location = ndb.StringProperty()

class Agreement(ndb.Model):
	ownerEmail = ndb.StringProperty()
	time = ndb.DateTimeProperty(auto_now_add=True)

class ReminderList(ndb.Model):
	number = ndb.StringProperty()
	event = ndb.KeyProperty(kind=Event)
	duration = ndb.StringProperty()
	#24 for 1 day before event, 1 for 1 hour before event, 3 for 3 hours before event, 12 for 12 hours before event

DEFAULT_NAME = 'default_thread'

JINJA_ENVIRONMENT = jinja2.Environment(
		loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
		extensions=['jinja2.ext.autoescape'],
		autoescape=True)
# [END imports]

class SetEvent(webapp2.RequestHandler):
	def get(self):
		target = self.request.get('owner')
		if len(target) > 0:
			result = TimeLine.query(TimeLine.ownerEmail == target)
			nameList = []
			for tl in result:
				nameList.append(tl.name)

			template = {
				'timelineList': nameList
			}
			self.response.write(json.dumps(template))

	def post(self):
		user = self.request.get('email')
		title = self.request.get('title')
		time = self.request.get('time')
		timeLine = self.request.get('timeLine')
		location = self.request.get('location')
		
		event = Event()
		event.ownerEmail = user
		event.title = title
		event.time = time

		tlKey = ""
		tls = TimeLine.query(TimeLine.ownerEmail == user)
		for tl in tls:
			if tl.name == timeLine:
				tlKey = tl.key.urlsafe()
				break

		event.timeLine = ndb.Key(urlsafe=tlKey)
		event.location = location
		event.put()
		self.response.write("Event created!")

class DeleteEvent(webapp2.RequestHandler):
	def post(self):
		user = self.request.get('email')
		title = self.request.get('title')
		time = self.request.get('time')
		location = self.request.get('location')
		timeLine_name = self.request.get('timeLine')

		events = Event.query(user == Event.ownerEmail)
		for e in events:
			if title == e.title and time == e.time and location == e.location:
				e.key.delete()
				break

		self.response.headers['Content-Type'] = 'text/plain'
		self.response.write('Delete success')

class CreateTimeLine(webapp2.RequestHandler):
	def post(self):
		user = self.request.get('email')
		name = self.request.get('timeLine')
		
		tls = TimeLine.query(user == TimeLine.ownerEmail)
		isExist = 0
		for t in tls:
			if name == t.name:
				isExist = 1
				break
		
		if isExist == 1:
			self.response.write("TimeLine already exist!")
		else:
			tl = TimeLine()
			tl.ownerEmail = user
			tl.name = name
			tl.put()
			self.response.write("TimeLine created!")

class DeleteTimeLine(webapp2.RequestHandler):
	def post(self):
		user = self.request.get('email')
		timeline = self.request.get('timeLine')
		tls = TimeLine.query(user == TimeLine.ownerEmail)
		target = ""
		# delete timeline
		for t in tls:
			if timeline == t.name:
				target = t
				t.key.delete()
				break

		# delete events
		events = Event.query(target.key == Event.timeLine)
		for e in events:
			e.key.delete()

		self.response.headers['Content-Type'] = 'text/plain'
		self.response.write('Delete success')

class SendSMS(webapp2.RequestHandler):
	def get(self):
		fm = "+17372104776"
		account_sid = "ACa7bca78c2961d1021f25ccf875b60301"
		auth_token = "4f2463ca8a235437727db6430c45a42d"
		client = Client(account_sid, auth_token)
		body = "You got an event: APT demo on 20171206."

		rv = client.messages.create(to="+15128031589", from_=fm, body=body)
		self.response.write(str(rv))

class GetEvent(webapp2.RequestHandler):
	def get(self):
		user = self.request.get('email')
		time = self.request.get('time')
		events = Event.query(user == Event.ownerEmail)
		titles = []
		locations = []

		for e in events:
			t = e.time
			if time == t[:8]:
				titles.append(e.title)
				locations.append(e.location)
			
		template = {
			'titles': titles,
			'locations': locations
		}
		self.response.write(json.dumps(template))

class GetEventByTimeLine(webapp2.RequestHandler):
	def get(self):
		user = self.request.get('email')
		target = self.request.get('timeline')
		tls = TimeLine.query(user == TimeLine.ownerEmail)
		names = []
		times = []
		t = ""
		for tl in tls:
			if target == tl.name:
				t = tl
				break

		events = Event.query(t.key == Event.timeLine)
		for e in events:
			names.append(e.title)
			times.append(e.time)

		template = {
			'titles': names,
			'times': times
		}
		self.response.write(json.dumps(template))

class ViewEvent(webapp2.RequestHandler):
	def get(self):
		user = self.request.get('email')
		title = self.request.get('title')
		date = self.request.get('date')
		events = Event.query(user == Event.ownerEmail)
		target = ""
		for e in events:
			if e.title == title and date == e.time[:8]:
				target = e
				break

		template = {
			'time': target.time,
			'location': target.location,
			'timeline': target.timeLine.get().name
		}
		self.response.write(json.dumps(template))

class AgreementHandler(webapp2.RequestHandler):
	def get(self):
		user = self.request.get('owner')
		result = Agreement.query(user == Agreement.ownerEmail)
		l = 0
		for r in result:
			l += 1

		if l > 0:
			result = "AGREE"
		else:
			result = "DISAGREE"

		template = {
			'result': result
		}
		self.response.write(json.dumps(template))

	def post(self):
		user = self.request.get('email')
		agree = Agreement()
		agree.ownerEmail = user
		agree.put()
		self.response.write('Agree Created!')

app = webapp2.WSGIApplication([
	('/setevent', SetEvent),
	('/timeline', CreateTimeLine),
	('/deletetl', DeleteTimeLine),
	('/sendsms', SendSMS),
	('/getevent', GetEvent),
	('/geteventbytl', GetEventByTimeLine),
	('/viewevent', ViewEvent),
	('/deleteevent', DeleteEvent),
	('/agree', AgreementHandler),
], debug=True)
