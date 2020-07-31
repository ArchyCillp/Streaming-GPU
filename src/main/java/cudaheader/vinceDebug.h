#pragma once
#include <stdio.h>

clock_t start, finish;
float costtime;
int timer_cnt = 0;

#define START_TIMER() start = clock()
#define END_TIMER() printf("%04d: Time elipse %f ms\n",++timer_cnt,(float)(clock()-start)/CLOCKS_PER_SEC)
#define RESTART_TIMER() END_TIMER();START_TIMER()