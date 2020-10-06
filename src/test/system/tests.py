import sys
import unittest
import subprocess
import re


class TestJSH(unittest.TestCase):

    JSH_IMAGE = "jsh-test"
    TEST_VOLUME = "jsh-system-test-volume"
    TEST_IMAGE = "jsh-system-test"
    TEST_DIR = "/system-test"

    @classmethod
    def eval(cls, cmdline, shell="/jsh/jsh"):
        volume = cls.TEST_VOLUME + ":" + cls.TEST_DIR + ":"
        args = ["docker", "run", "--rm", "-v", volume, cls.TEST_IMAGE, shell, "-c", cmdline]
        p = subprocess.run(args, capture_output=True)
        return p.stdout.decode()

    @classmethod
    def setUpClass(cls):
        dockerfile = ("FROM " + cls.JSH_IMAGE + "\nWORKDIR " + cls.TEST_DIR).encode()
        args = ["docker", "build", "-t", cls.TEST_IMAGE, "-"]
        p = subprocess.run(args, input=dockerfile, stdout=subprocess.DEVNULL)
        if (p.returncode != 0):
            print("error: failed to build test image")
            exit(1)

    @classmethod
    def tearDownClass(cls):
        p = subprocess.run(["docker", "image", "rm", cls.TEST_IMAGE], stdout=subprocess.DEVNULL)
        if (p.returncode != 0):
            print("error: failed to remove test image")
            exit(1)

    def setUp(self):
        p = subprocess.run(["docker", "volume", "create", self.TEST_VOLUME], stdout=subprocess.DEVNULL)
        if (p.returncode != 0):
            print("error: failed to create test volume")
            exit(1)
        filesystem_setup = ";".join([
            "echo \"AAA\" > test.txt",
            "mkdir dir1",
            "mkdir dir2",
            "echo DEF > dir1/file.txt",
            "echo ABC >> dir1/file.txt",
            "echo ABC >> dir1/file.txt",
            "for i in {1..20}; do echo $i >> dir1/longfile.txt; done",
            "echo ABC > dir2/file.txt",
            "echo GHI >> dir2/file.txt"
        ])
        self.eval(filesystem_setup, shell="/bin/bash")

    def tearDown(self):
        p = subprocess.run(["docker", "volume", "rm", self.TEST_VOLUME], stdout=subprocess.DEVNULL)
        if (p.returncode != 0):
            print("error: failed to remove test volume")
            exit(1)

    def test_echo(self):
        cmdline = "echo hello world"
        stdout = self.eval(cmdline)
        result = stdout.strip()
        self.assertEqual(result, "hello world")

    def test_ls(self):
        cmdline = "ls"
        stdout = self.eval(cmdline)
        result = set(re.split("\n|\t", stdout.strip()))
        self.assertEqual(result, {"test.txt", "dir1", "dir2"})

    def test_pwd(self):
        cmdline = "pwd"
        stdout = self.eval(cmdline)
        result = stdout.strip()
        self.assertEqual(result, self.TEST_DIR)

    def test_cd_pwd(self):
        cmdline = "cd dir1; pwd"
        stdout = self.eval(cmdline)
        result = stdout.strip()
        self.assertEqual(result, self.TEST_DIR + "/dir1")

    def test_cat(self):
        cmdline = "cat dir1/file.txt dir2/file.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split("\n")
        self.assertEqual(result, ["DEF", "ABC", "ABC", "ABC", "GHI"])

    def test_head(self):
        cmdline = "head dir1/longfile.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split("\n")
        self.assertEqual(result, [str(i) for i in range(1,11)])

    def test_tail(self):
        cmdline = "tail dir1/longfile.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split("\n")
        self.assertEqual(result, [str(i) for i in range(11,21)])

    def test_grep(self):
        cmdline = "grep ABC dir1/file.txt dir2/file.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split("\n")
        self.assertEqual(result, ["dir1/file.txt:ABC", "dir1/file.txt:ABC", "dir2/file.txt:ABC"])

    def test_cut(self):
        cmdline = "cut -b 1,3 dir1/file.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split("\n")
        self.assertEqual(result, ["DF", "AC", "AC"])

    def test_find(self):
        cmdline = "find -name file.txt"
        stdout = self.eval(cmdline)
        result = set(re.split("\n|\t", stdout.strip()))
        self.assertEqual(result, {"./dir1/file.txt", "./dir2/file.txt"})
    
    def test_uniq(self):
        cmdline = "uniq dir1/file.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split()
        self.assertEqual(result, ["DEF", "ABC"])

    def test_sort(self):
        cmdline = "sort dir1/file.txt"
        stdout = self.eval(cmdline)
        result = stdout.strip().split()
        self.assertEqual(result, ["ABC", "ABC", "DEF"])


if __name__ == '__main__':
    unittest.main()
