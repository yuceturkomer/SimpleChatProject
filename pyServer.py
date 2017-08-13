import fcntl, os
import errno
from time import sleep
import socket
import sys
from thread import *

HOST = 'localhost'   # Symbolic name meaning all available interfaces
PORT = 21200 # Arbitrary non-privileged port

count = 0
connections = []
addresses = []
People = []
threads = []

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print 'Socket created'

#Bind socket to local host and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print 'Bind failed. Error Code : ' + str(msg[0]) + ' Message ' + msg[1]
    sys.exit()

print 'Socket bind complete'

#Start listening on socket
s.listen(10)
print 'Socket now listening'

#Function for handling connections. This will be used to create threads
def clientthread(connFirst, connSecond):
    fcntl.fcntl(connFirst, fcntl.F_SETFL, os.O_NONBLOCK)
    fcntl.fcntl(connSecond, fcntl.F_SETFL, os.O_NONBLOCK)
    print HOST , "   aaaand    " , PORT
    while True:
        #First msg recv for the first person. If there is a message, send it to the second
        try:
            msg = connFirst.recv(4096)
        except socket.error, e:
            err = e.args[0]
            if err != errno.EAGAIN and err != errno.EWOULDBLOCK:
                # a "real" error occurred
                print e
                sys.exit(1)
        else:
        # got a message, do something :)
            connSecond.send(msg)

        # First msg recv for the second person. If there is a message, send it to the first
        try:
            msg = connSecond.recv(4096)
        except socket.error, e:
            err = e.args[0]
            if err != errno.EAGAIN and err != errno.EWOULDBLOCK:
                # a "real" error occurred
                print e
                sys.exit(1)
        else:
        # got a message, do something :)
            connFirst.send(msg)
    print "Connection closed"
    #came out of loop
    conn.close()
    interrupt_main()
    exit()


try:
    #now keep talking with the client
    while True:
        #wait to accept a connection - blocking call
        conn, addr = s.accept()
        print 'Connected with ' + addr[0] + ':' + str(addr[1])
        connections.append(conn)
        addresses.append(addr)

        data = conn.recv(1024)
        if not data:
            break
        People.append(data)
        count+=1
        if count % 2 == 0:
            #start new thread takes 1st argument as a function name to be run, second is the tuple of arguments to the function.
            threads.append(start_new_thread(clientthread ,(connections[connections.__len__()-2],connections[connections.__len__()-1])))
except KeyboardInterrupt:
    print('You cancelled the operation.')
    s.close()
    sys.exit()


