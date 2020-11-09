#ifndef UTILITIES_HPP
#define UTILITIES_HPP

#include <string>
#include <ctime>
using namespace std;

namespace Custom {
    static string getTime() {
        time_t now;
        struct tm local;
        now = time(NULL);
        local = *localtime(&now);

        string h = to_string(local.tm_hour);
        string m = to_string(local.tm_min);
        string s = local.tm_sec < 10 ? '0' + to_string(local.tm_sec) : to_string(local.tm_sec);
        string d = to_string(local.tm_mday);
        string t = to_string(local.tm_mon + 1);
        string y = to_string(local.tm_year + 1900);


        string buffer = "";
        buffer += '[' + h + ':' + m + ':' + s + "] " + d + '/' + t + '/' + y + " > ";
        return buffer;
    }
}

#endif