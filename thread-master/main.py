#!/usr/bin/env python

# [START imports]
import os

from google.appengine.api import users
from google.appengine.api import mail
from google.appengine.ext import ndb

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


DEFAULT_NAME = 'default_thread'

JINJA_ENVIRONMENT = jinja2.Environment(
		loader=jinja2.FileSystemLoader(os.path.dirname(__file__)),
		extensions=['jinja2.ext.autoescape'],
		autoescape=True)
# [END imports]

api_key = "AAAAVc8-YuM:APA91bGsZyeiPp13SQdI43MVuv2DO0SviHHMunZGpFN0pXD5XyUuBiFwgp4ZXiakhjctFdidoRTGVOUetSTbr-ZjXfpvHW6RU_x79y0efyiHTH3sHWbyA-i0cYvo4dppliT_KUOtubYw"

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

app = webapp2.WSGIApplication([
	('/setevent', SetEvent),
	('/timeline', CreateTimeLine),
	('/deletetl', DeleteTimeLine),
], debug=True)
