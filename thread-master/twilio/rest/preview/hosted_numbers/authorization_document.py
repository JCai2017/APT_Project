# coding=utf-8
"""
This code was generated by
\ / _    _  _|   _  _
 | (_)\/(_)(_|\/| |(/_  v1.0.0
      /       /
"""

from twilio.base import deserialize
from twilio.base import serialize
from twilio.base import values
from twilio.base.instance_context import InstanceContext
from twilio.base.instance_resource import InstanceResource
from twilio.base.list_resource import ListResource
from twilio.base.page import Page


class AuthorizationDocumentList(ListResource):
    """ PLEASE NOTE that this class contains preview products that are subject
    to change. Use them with caution. If you currently do not have developer
    preview access, please contact help@twilio.com. """

    def __init__(self, version):
        """
        Initialize the AuthorizationDocumentList

        :param Version version: Version that contains the resource

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentList
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentList
        """
        super(AuthorizationDocumentList, self).__init__(version)

        # Path Solution
        self._solution = {}
        self._uri = '/AuthorizationDocuments'.format(**self._solution)

    def stream(self, email=values.unset, status=values.unset, limit=None,
               page_size=None):
        """
        Streams AuthorizationDocumentInstance records from the API as a generator stream.
        This operation lazily loads records as efficiently as possible until the limit
        is reached.
        The results are returned as a generator, so this operation is memory efficient.

        :param unicode email: Email.
        :param AuthorizationDocumentInstance.Status status: The Status of this AuthorizationDocument.
        :param int limit: Upper limit for the number of records to return. stream()
                          guarantees to never return more than limit.  Default is no limit
        :param int page_size: Number of records to fetch per request, when not set will use
                              the default value of 50 records.  If no page_size is defined
                              but a limit is defined, stream() will attempt to read the
                              limit with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        :rtype: list[twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance]
        """
        limits = self._version.read_limits(limit, page_size)

        page = self.page(email=email, status=status, page_size=limits['page_size'])

        return self._version.stream(page, limits['limit'], limits['page_limit'])

    def list(self, email=values.unset, status=values.unset, limit=None,
             page_size=None):
        """
        Lists AuthorizationDocumentInstance records from the API as a list.
        Unlike stream(), this operation is eager and will load `limit` records into
        memory before returning.

        :param unicode email: Email.
        :param AuthorizationDocumentInstance.Status status: The Status of this AuthorizationDocument.
        :param int limit: Upper limit for the number of records to return. list() guarantees
                          never to return more than limit.  Default is no limit
        :param int page_size: Number of records to fetch per request, when not set will use
                              the default value of 50 records.  If no page_size is defined
                              but a limit is defined, list() will attempt to read the limit
                              with the most efficient page size, i.e. min(limit, 1000)

        :returns: Generator that will yield up to limit results
        :rtype: list[twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance]
        """
        return list(self.stream(email=email, status=status, limit=limit, page_size=page_size))

    def page(self, email=values.unset, status=values.unset, page_token=values.unset,
             page_number=values.unset, page_size=values.unset):
        """
        Retrieve a single page of AuthorizationDocumentInstance records from the API.
        Request is executed immediately

        :param unicode email: Email.
        :param AuthorizationDocumentInstance.Status status: The Status of this AuthorizationDocument.
        :param str page_token: PageToken provided by the API
        :param int page_number: Page Number, this value is simply for client state
        :param int page_size: Number of records to return, defaults to 50

        :returns: Page of AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentPage
        """
        params = values.of({
            'Email': email,
            'Status': status,
            'PageToken': page_token,
            'Page': page_number,
            'PageSize': page_size,
        })

        response = self._version.page(
            'GET',
            self._uri,
            params=params,
        )

        return AuthorizationDocumentPage(self._version, response, self._solution)

    def get_page(self, target_url):
        """
        Retrieve a specific page of AuthorizationDocumentInstance records from the API.
        Request is executed immediately

        :param str target_url: API-generated URL for the requested results page

        :returns: Page of AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentPage
        """
        response = self._version.domain.twilio.request(
            'GET',
            target_url,
        )

        return AuthorizationDocumentPage(self._version, response, self._solution)

    def create(self, hosted_number_order_sids, address_sid, email,
               cc_emails=values.unset):
        """
        Create a new AuthorizationDocumentInstance

        :param unicode hosted_number_order_sids: A list of HostedNumberOrder sids.
        :param unicode address_sid: Address sid.
        :param unicode email: Email.
        :param unicode cc_emails: A list of emails.

        :returns: Newly created AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        data = values.of({
            'HostedNumberOrderSids': serialize.map(hosted_number_order_sids, lambda e: e),
            'AddressSid': address_sid,
            'Email': email,
            'CcEmails': serialize.map(cc_emails, lambda e: e),
        })

        payload = self._version.create(
            'POST',
            self._uri,
            data=data,
        )

        return AuthorizationDocumentInstance(self._version, payload)

    def get(self, sid):
        """
        Constructs a AuthorizationDocumentContext

        :param sid: AuthorizationDocument sid.

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        """
        return AuthorizationDocumentContext(self._version, sid=sid)

    def __call__(self, sid):
        """
        Constructs a AuthorizationDocumentContext

        :param sid: AuthorizationDocument sid.

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        """
        return AuthorizationDocumentContext(self._version, sid=sid)

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Preview.HostedNumbers.AuthorizationDocumentList>'


class AuthorizationDocumentPage(Page):
    """ PLEASE NOTE that this class contains preview products that are subject
    to change. Use them with caution. If you currently do not have developer
    preview access, please contact help@twilio.com. """

    def __init__(self, version, response, solution):
        """
        Initialize the AuthorizationDocumentPage

        :param Version version: Version that contains the resource
        :param Response response: Response from the API

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentPage
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentPage
        """
        super(AuthorizationDocumentPage, self).__init__(version, response)

        # Path Solution
        self._solution = solution

    def get_instance(self, payload):
        """
        Build an instance of AuthorizationDocumentInstance

        :param dict payload: Payload response from the API

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        return AuthorizationDocumentInstance(self._version, payload)

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        return '<Twilio.Preview.HostedNumbers.AuthorizationDocumentPage>'


class AuthorizationDocumentContext(InstanceContext):
    """ PLEASE NOTE that this class contains preview products that are subject
    to change. Use them with caution. If you currently do not have developer
    preview access, please contact help@twilio.com. """

    def __init__(self, version, sid):
        """
        Initialize the AuthorizationDocumentContext

        :param Version version: Version that contains the resource
        :param sid: AuthorizationDocument sid.

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        """
        super(AuthorizationDocumentContext, self).__init__(version)

        # Path Solution
        self._solution = {'sid': sid}
        self._uri = '/AuthorizationDocuments/{sid}'.format(**self._solution)

    def fetch(self):
        """
        Fetch a AuthorizationDocumentInstance

        :returns: Fetched AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        params = values.of({})

        payload = self._version.fetch(
            'GET',
            self._uri,
            params=params,
        )

        return AuthorizationDocumentInstance(self._version, payload, sid=self._solution['sid'])

    def update(self, hosted_number_order_sids=values.unset,
               address_sid=values.unset, email=values.unset, cc_emails=values.unset,
               status=values.unset):
        """
        Update the AuthorizationDocumentInstance

        :param unicode hosted_number_order_sids: A list of HostedNumberOrder sids.
        :param unicode address_sid: Address sid.
        :param unicode email: Email.
        :param unicode cc_emails: A list of emails.
        :param AuthorizationDocumentInstance.Status status: The Status of this AuthorizationDocument.

        :returns: Updated AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        data = values.of({
            'HostedNumberOrderSids': serialize.map(hosted_number_order_sids, lambda e: e),
            'AddressSid': address_sid,
            'Email': email,
            'CcEmails': serialize.map(cc_emails, lambda e: e),
            'Status': status,
        })

        payload = self._version.update(
            'POST',
            self._uri,
            data=data,
        )

        return AuthorizationDocumentInstance(self._version, payload, sid=self._solution['sid'])

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Preview.HostedNumbers.AuthorizationDocumentContext {}>'.format(context)


class AuthorizationDocumentInstance(InstanceResource):
    """ PLEASE NOTE that this class contains preview products that are subject
    to change. Use them with caution. If you currently do not have developer
    preview access, please contact help@twilio.com. """

    class Status(object):
        OPENED = "opened"
        SIGNING = "signing"
        SIGNED = "signed"
        CANCELED = "canceled"
        FAILED = "failed"

    def __init__(self, version, payload, sid=None):
        """
        Initialize the AuthorizationDocumentInstance

        :returns: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        super(AuthorizationDocumentInstance, self).__init__(version)

        # Marshaled Properties
        self._properties = {
            'sid': payload['sid'],
            'address_sid': payload['address_sid'],
            'status': payload['status'],
            'email': payload['email'],
            'cc_emails': payload['cc_emails'],
            'date_created': deserialize.iso8601_datetime(payload['date_created']),
            'date_updated': deserialize.iso8601_datetime(payload['date_updated']),
            'url': payload['url'],
        }

        # Context
        self._context = None
        self._solution = {'sid': sid or self._properties['sid']}

    @property
    def _proxy(self):
        """
        Generate an instance context for the instance, the context is capable of
        performing various actions.  All instance actions are proxied to the context

        :returns: AuthorizationDocumentContext for this AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentContext
        """
        if self._context is None:
            self._context = AuthorizationDocumentContext(self._version, sid=self._solution['sid'])
        return self._context

    @property
    def sid(self):
        """
        :returns: AuthorizationDocument sid.
        :rtype: unicode
        """
        return self._properties['sid']

    @property
    def address_sid(self):
        """
        :returns: Address sid.
        :rtype: unicode
        """
        return self._properties['address_sid']

    @property
    def status(self):
        """
        :returns: The Status of this AuthorizationDocument.
        :rtype: AuthorizationDocumentInstance.Status
        """
        return self._properties['status']

    @property
    def email(self):
        """
        :returns: Email.
        :rtype: unicode
        """
        return self._properties['email']

    @property
    def cc_emails(self):
        """
        :returns: A list of emails.
        :rtype: unicode
        """
        return self._properties['cc_emails']

    @property
    def date_created(self):
        """
        :returns: The date this AuthorizationDocument was created.
        :rtype: datetime
        """
        return self._properties['date_created']

    @property
    def date_updated(self):
        """
        :returns: The date this AuthorizationDocument was updated.
        :rtype: datetime
        """
        return self._properties['date_updated']

    @property
    def url(self):
        """
        :returns: The url
        :rtype: unicode
        """
        return self._properties['url']

    def fetch(self):
        """
        Fetch a AuthorizationDocumentInstance

        :returns: Fetched AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        return self._proxy.fetch()

    def update(self, hosted_number_order_sids=values.unset,
               address_sid=values.unset, email=values.unset, cc_emails=values.unset,
               status=values.unset):
        """
        Update the AuthorizationDocumentInstance

        :param unicode hosted_number_order_sids: A list of HostedNumberOrder sids.
        :param unicode address_sid: Address sid.
        :param unicode email: Email.
        :param unicode cc_emails: A list of emails.
        :param AuthorizationDocumentInstance.Status status: The Status of this AuthorizationDocument.

        :returns: Updated AuthorizationDocumentInstance
        :rtype: twilio.rest.preview.hosted_numbers.authorization_document.AuthorizationDocumentInstance
        """
        return self._proxy.update(
            hosted_number_order_sids=hosted_number_order_sids,
            address_sid=address_sid,
            email=email,
            cc_emails=cc_emails,
            status=status,
        )

    def __repr__(self):
        """
        Provide a friendly representation

        :returns: Machine friendly representation
        :rtype: str
        """
        context = ' '.join('{}={}'.format(k, v) for k, v in self._solution.items())
        return '<Twilio.Preview.HostedNumbers.AuthorizationDocumentInstance {}>'.format(context)
