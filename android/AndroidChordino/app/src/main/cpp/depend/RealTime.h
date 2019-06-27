//
// Created by Administrator on 2019/6/17.
//

#ifndef ANDROIDCHORDINO_REALTIME_H
#define ANDROIDCHORDINO_REALTIME_H

#ifndef _VAMP_REAL_TIME_H_
#define _VAMP_REAL_TIME_H_

#include <iostream>
#include <string>

#ifndef _WIN32
struct timeval;
#endif

namespace Vamp {

/**
 * \class RealTime RealTime.h <vamp-sdk/RealTime.h>
 *
 * RealTime represents time values to nanosecond precision
 * with accurate arithmetic and frame-rate conversion functions.
 */

    struct RealTime
    {
        int sec;
        int nsec;

        int usec() const { return nsec / 1000; }
        int msec() const { return nsec / 1000000; }

        RealTime(): sec(0), nsec(0) {}
        RealTime(int s, int n);

        RealTime(const RealTime &r) :
                sec(r.sec), nsec(r.nsec) { }

        static RealTime fromSeconds(double sec);
        static RealTime fromMilliseconds(int msec);

#ifndef _WIN32
        static RealTime fromTimeval(const struct timeval &);
#endif

        RealTime &operator=(const RealTime &r) {
            sec = r.sec; nsec = r.nsec; return *this;
        }

        RealTime operator+(const RealTime &r) const {
            return RealTime(sec + r.sec, nsec + r.nsec);
        }
        RealTime operator-(const RealTime &r) const {
            return RealTime(sec - r.sec, nsec - r.nsec);
        }
        RealTime operator-() const {
            return RealTime(-sec, -nsec);
        }

        bool operator <(const RealTime &r) const {
            if (sec == r.sec) return nsec < r.nsec;
            else return sec < r.sec;
        }

        bool operator >(const RealTime &r) const {
            if (sec == r.sec) return nsec > r.nsec;
            else return sec > r.sec;
        }

        bool operator==(const RealTime &r) const {
            return (sec == r.sec && nsec == r.nsec);
        }

        bool operator!=(const RealTime &r) const {
            return !(r == *this);
        }

        bool operator>=(const RealTime &r) const {
            if (sec == r.sec) return nsec >= r.nsec;
            else return sec >= r.sec;
        }

        bool operator<=(const RealTime &r) const {
            if (sec == r.sec) return nsec <= r.nsec;
            else return sec <= r.sec;
        }

        RealTime operator/(int d) const;

        /**
         * Return the ratio of two times.
         */
        double operator/(const RealTime &r) const;

        /**
         * Return a human-readable debug-type string to full precision
         * (probably not a format to show to a user directly)
         */
        std::string toString() const;

        /**
         * Return a user-readable string to the nearest millisecond
         * in a form like HH:MM:SS.mmm
         */
        std::string toText(bool fixedDp = false) const;

        /**
         * Convert a RealTime into a sample frame at the given sample rate.
         */
        static long realTime2Frame(const RealTime &r, unsigned int sampleRate);

        /**
         * Convert a sample frame at the given sample rate into a RealTime.
         */
        static RealTime frame2RealTime(long frame, unsigned int sampleRate);

        static const RealTime zeroTime;
    };

    std::ostream &operator<<(std::ostream &out, const RealTime &rt);

}

#endif


#endif //ANDROIDCHORDINO_REALTIME_H
