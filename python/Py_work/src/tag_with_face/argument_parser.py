import sys

PARSE_FAILED = 1
PARSE_SUCCESS = 0


# cmd: python cmd_proxy.py [cmd] [ -input=xx -output=xxx ]
# ps: cmd must use + to replace ' '
class ArgumentParser(object):

    def __init__(self, include_extra_cmd=True, in_str="-input=", out_str="-output="):
        self.include_extra_cmd = include_extra_cmd
        self.inStr = in_str
        self.outStr = out_str
        self.ios = []
        self.cmd = ''  # cmd template

    def parse(self, args):
        extra_count = 2 if self.include_extra_cmd else 1
        agrs_count = len(args)
        if agrs_count == extra_count:
            print("argument count error. need at least %s" % (extra_count + 2), file=sys.stderr)
        elif (agrs_count - extra_count) % 2 != 0:
            print("argument count error.", file=sys.stderr)
        else:
            if self.include_extra_cmd:
                self.cmd = args[extra_count - 1].replace("+", " ")
            index = 2 if self.include_extra_cmd else 1
            in_len = len(self.inStr)
            out_len = len(self.outStr)
            while index < agrs_count:
                inputStr = args[index].strip()
                outputStr = args[index + 1].strip()
                if not inputStr.startswith(self.inStr):
                    print("argument error. inputStr = %s" % inputStr, file=sys.stderr)
                    return PARSE_FAILED
                if not outputStr.startswith(self.outStr):
                    print("argument error. outputStr = %s" % outputStr, file=sys.stderr)
                    return PARSE_FAILED
                self.ios.append(ArgumentIo(inputStr[in_len:], outputStr[out_len:]))
                index += 2
            return PARSE_SUCCESS

        return PARSE_FAILED

    def iterate_arguments(self):
        for i in range(0, len(self.ios)):
            yield i, self.ios[i]

    def get_extra_cmd(self):
        return self.cmd


class ArgumentIo(object):

    def __init__(self, arg_in, arg_out):
        self.arg_in = arg_in
        self.arg_out = arg_out

    def format_template(self, template):
        return template % (self.arg_in, self.arg_out)

