//
// Created by Administrator on 2019/7/15.
//


#include    <string.h>
#include    <limits.h>
#include    <math.h>
#include "float32.h"

#define _absi_(v) \
    (v >= 0 ? v : -v)

#ifdef __CPLUSPLUS
extern "C"{
#endif

int checkCPU(){
    union w
    {
        int a;
        char b;
    }c;
    c.a = 1;
    return (c.b == 1); // 小端返回TRUE,大端返回FALSE
}

float
float32_be_read(const unsigned char *cptr) {
    int exponent, mantissa, negative;
    float fvalue;

    negative = cptr[0] & 0x80;
    exponent = ((cptr[0] & 0x7F) << 1) | ((cptr[1] & 0x80) ? 1 : 0);
    mantissa = ((cptr[1] & 0x7F) << 16) | (cptr[2] << 8) | (cptr[3]);

    if (!(exponent || mantissa))
        return 0.0;

    mantissa |= 0x800000;
    exponent = exponent ? exponent - 127 : 0;

    fvalue = mantissa ? ((float) mantissa) / ((float) 0x800000) : 0.0;

    if (negative)
        fvalue *= -1;

    if (exponent > 0)
        fvalue *= pow(2.0, exponent);
    else if (exponent < 0)
        fvalue /= pow(2.0, _absi_ (exponent));

    return fvalue;
} /* float32_be_read */

float
float32_le_read(const unsigned char *cptr) {
    int exponent, mantissa, negative;
    float fvalue;

    negative = cptr[3] & 0x80;
    exponent = ((cptr[3] & 0x7F) << 1) | ((cptr[2] & 0x80) ? 1 : 0);
    mantissa = ((cptr[2] & 0x7F) << 16) | (cptr[1] << 8) | (cptr[0]);

    if (!(exponent || mantissa))
        return 0.0;

    mantissa |= 0x800000;
    exponent = exponent ? exponent - 127 : 0;

    fvalue = mantissa ? ((float) mantissa) / ((float) 0x800000) : 0.0;

    if (negative)
        fvalue *= -1;

    if (exponent > 0)
        fvalue *= pow(2.0, exponent);
    else if (exponent < 0)
        fvalue /= pow(2.0, _absi_ (exponent));

    return fvalue;
} /* float32_le_read */

void
float32_le_write(float in, unsigned char *out) {
    int exponent, mantissa, negative = 0;

    memset(out, 0, sizeof(int));

    if (fabs(in) < 1e-30)
        return;

    if (in < 0.0) {
        in *= -1.0;
        negative = 1;
    };

    in = frexp(in, &exponent);

    exponent += 126;

    in *= (float) 0x1000000;
    mantissa = (((int) in) & 0x7FFFFF);

    if (negative)
        out[3] |= 0x80;

    if (exponent & 0x01)
        out[2] |= 0x80;

    out[0] = mantissa & 0xFF;
    out[1] = (mantissa >> 8) & 0xFF;
    out[2] |= (mantissa >> 16) & 0x7F;
    out[3] |= (exponent >> 1) & 0x7F;

    return;
} /* float32_le_write */

void
float32_be_write(float in, unsigned char *out) {
    int exponent, mantissa, negative = 0;

    memset(out, 0, sizeof(int));

    if (fabs(in) < 1e-30)
        return;

    if (in < 0.0) {
        in *= -1.0;
        negative = 1;
    };

    in = frexp(in, &exponent);

    exponent += 126;

    in *= (float) 0x1000000;
    mantissa = (((int) in) & 0x7FFFFF);

    if (negative)
        out[0] |= 0x80;

    if (exponent & 0x01)
        out[1] |= 0x80;

    out[3] = mantissa & 0xFF;
    out[2] = (mantissa >> 8) & 0xFF;
    out[1] |= (mantissa >> 16) & 0x7F;
    out[0] |= (exponent >> 1) & 0x7F;

    return;
} /* float32_be_write */

#ifdef __CPLUSPLUS
}
#endif