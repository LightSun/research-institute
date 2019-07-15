//
// Created by Administrator on 2019/7/15.
//

//copy from snd.

float float32_be_read (const unsigned char *cptr);
float float32_le_read (const unsigned char *cptr);
void float32_le_write (float in, unsigned char *out);
void float32_be_write (float in, unsigned char *out);

int checkCPU();

#if checkCPU == 1
    #define FLOAT32_READ	float32_le_read
	#define FLOAT32_WRITE	float32_le_write
#else
    #define FLOAT32_READ	float32_be_read
	#define FLOAT32_WRITE	float32_be_write
#endif
