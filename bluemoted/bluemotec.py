#!/usr/bin/env python
# file: rfcomm-client.py
# auth: Albert Huang <albert@csail.mit.edu>
# desc: simple demonstration of a client application that uses RFCOMM sockets
#       intended for use with rfcomm-server
#
# $Id: rfcomm-client.py 424 2006-08-24 03:35:54Z albert $

print 0

from bluetooth import *
import sys

addr = None

if len(sys.argv) < 2:
    print "no device specified.  Searching all nearby bluetooth devices for"
    print "the SampleServer service"
else:
    addr = sys.argv[1]
    print "Searching for SampleServer on %s" % addr

# search for the SampleServer service
uuid = "00001101-0000-1000-8000-00805F9B34F9"
while True:
    service_matches = find_service( uuid = uuid, address = addr)
    if len(service_matches) > 0: break

if len(service_matches) > 1:
    print service_matches
    
first_match = service_matches[0]
port = first_match["port"]
name = first_match["name"]
host = first_match["host"]

print "connecting to \"%s\" on %s %s" % (name, host, port)

# Create the client socket
sock=BluetoothSocket( RFCOMM )
print 3
sock.connect((host, port))

print "connected.  type stuff"
while True:
    data = raw_input()
    if len(data) == 0: break
    sock.send(data)

sock.close()

