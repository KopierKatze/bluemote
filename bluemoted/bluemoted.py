#!/usr/bin/env python

def main():
    keyrelay = KeyRelay()
    rfcomm = RFCommServer(keyrelay.xte_cmd)
    rfcomm.run()

####

from subprocess import Popen, PIPE

class KeyRelay:

    def __init__(self):
        self.xte = Popen(['xte'], stdin=PIPE)

    def hit_key(self, key):
        self.xte_cmd("key {0}".format(key))

    def xte_cmd(self, input):
        self.xte.communicate(input=input)

####

from bluetooth import *

class RFCommServer:

    def __init__(self, on_data):
        self.channel = 1
        self.uuid = "c6a54615-3868-4b38-bfa3-8aa0d6d2b9f5"
        self.on_data = on_data

    def run(self):
        self.server_sock = server_sock = BluetoothSocket( RFCOMM )
        server_sock.bind(("", PORT_ANY))
        server_sock.listen(self.channel)
        self.port = server_sock.getsockname()[1]

        advertise_service(
            server_sock, "BlueMoteD",
            service_id = self.uuid,
            service_classes = [ self.uuid, SERIAL_PORT_CLASS ],
            profiles = [ SERIAL_PORT_PROFILE ],
        )

        while True: self.accept_conn()

    def accept_conn(self):
        print "Waiting for connection on RFCOMM channel {0} with uuid {1}".format(self.port, self.uuid)
        client_sock, client_info = self.server_sock.accept()
        print "Accepted connection from ", client_info
        try:
            while self.listen_for_data(client_sock): pass
        except IOError as e:
            print e
        client_sock.close()
        self.server_sock.close()
        print "disconnected"

    def listen_for_data(client_sock):
        data = client_sock.recv(1024)
        if len(data) == 0: return False
        print "received [%s]" % data
        self.on_data(data)
        return True

####

if __name__ == '__main__': main()

