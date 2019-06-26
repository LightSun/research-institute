//
// Created by Administrator on 2019/6/25.
//

#ifndef VAMPCOMPILEAPP_MATH_H
#define VAMPCOMPILEAPP_MATH_H

#define DBL_EPSILON 0.000 0001
#define PI 3.14159265

double fabs(double val){
   return val >= 0 ? val : -val;
}

//牛顿法
double sqrt(double A)
{
	double x0 = A + 0.25, x1, xx = x0;
	for(;;) {
		x1 = (x0*x0 + A) / (2*x0);
		if(fabs(x1 - x0) <= DBL_EPSILON) break;
		if(xx == x1) break; // to break two value cycle.
		xx = x0;
		x0 = x1;
	}

	return x1;
}

#endif //VAMPCOMPILEAPP_MATH_H
