#!/usr/bin/env python

### Tests
# * Keyboard Interrupt
# * Address already in use
# * 
#

def main():
    keyrelay = KeyRelay()
    rfcomm = RFCommServer(keyrelay.xte_cmd)
    rfcomm.run()

####

from subprocess import Popen, PIPE

class KeyRelay:

    def __init__(self):
        self.setup()

    def setup(self):
        self.xte = Popen(['xte'], stdin=PIPE)

    def hit_key(self, key):
        self.xte_cmd("key {0}".format(key))

    def xte_cmd(self, input):
        print repr(input)
        #self.xte.communicate(input=input)
        if input[-1] != '\n': input += '\n'
        try: self.xte.stdin.write(input)
        except:
            try: self.xte.close()
            except e: print e
            self.setup()
            self.xte.stdin.write(input)

####

from bluetooth import *
from random import randrange

class RFCommServer:

    def __init__(self, on_data):
        self.channel = 6
        self.uuid = "00001101-0000-1000-8000-00805F9B34F9"
        self.on_data = on_data
        self.state = "setup"
        self.stop = False

    def run(self):
        while not self.stop:
            try:
                if getattr(self, self.state)() is False:
                    break
            except KeyboardInterrupt: self.stop = True
            except BluetoothError as e:
                print "BluetoothError in 'run':", e
                self.state = "setup" # try again
            except Exception as e:
                try:
                    if e[0] == 98: # Address already in use -> stop
                        print e[1]
                        self.stop = True
                except:
                    print "Error in 'run':", e
                    self.state = "setup"

    def setup(self):
        self.server_sock = server_sock = BluetoothSocket( RFCOMM )
        server_sock.bind(("", self.channel))
        server_sock.listen(self.channel)
        self.port = server_sock.getsockname()[1]

        advertise_service(
            server_sock, "BlueMoteD",
            service_id = self.uuid,
            service_classes = [ self.uuid, SERIAL_PORT_CLASS ],
            profiles = [ SERIAL_PORT_PROFILE ],
        )
        print "advertising BlueMoteD"
        self.state = "accept"
    
    def accept(self):
        print "Waiting for connection on RFCOMM channel {0} with uuid {1}".format(self.port, self.uuid)
        self.client_sock, self.client_info = self.server_sock.accept()
        print "Accepted connection from ", self.client_info
        self.state = "read"

    def read(self):
        try:
            while self.listen_for_data(self.client_sock): pass
        except KeyboardInterrupt:
            pass
        except:
            print sys.exc_info()[0]
        finally:
            self.state = "close"

    def close(self):
        self.client_sock.close()
        print "disconnected"

    def listen_for_data(self):
        data = self.client_sock.recv(1024)
        self.on_data(data)
        return True

####

if __name__ == '__main__': main()

