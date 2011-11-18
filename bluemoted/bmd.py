
from subprocess import Popen, PIPE

class KeyRelay:

    def __init__(self):
        self.xte = Popen(['xte'], stdin=PIPE)

    def hit_key(self, key):
        self.xte_cmd("key {0}".format(key))

    def xte_cmd(self, input):
        #self.xte.communicate(input=input)
        self.xte.stdin.write(input)
        
####

from wsgiref.simple_server import make_server

class BluemoteHttp:

    def __init__(self, output):
        self.output = output

    def __call__(self, environ, start_response):
        method = environ.get('REQUEST_METHOD')
        path   = environ.get('PATH_INFO') or '/'
        infile = environ['wsgi.input']
        length = int(environ.get('CONTENT_LENGTH', 0))

        if method == 'POST':
            if path == '/cmd':
                self.output(infile.read(length))
                status = '200 OK'
                headers = [('Content-type', 'text/plain')]
                start_response(status, headers)
                return 'OK'

        status = '404 Not Found'
        headers = [('Content-type', 'text/plain')]
        start_response(status, headers)
        return status

def main():
    keyrelay = KeyRelay()
    bluemote_http = BluemoteHttp(keyrelay.xte_cmd)
    httpd = make_server('', 4242, bluemote_http)
    print "Serving on port 4242..."
    httpd.serve_forever()

if __name__ == '__main__': main()
