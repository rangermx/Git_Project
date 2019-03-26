#ifndef __DEF_H__
#define __DEF_H__
/**
 * 定义状态机结构体
 * 参数为函数指针，指向当前状态的输入处理函数。
 * buffer1DataHandle 处理buffer1接收到的数据，参数1 buffer首地址， 参数2 数据长度
 * buffer2DataHandle 处理buffer2接受到的数据，参数1 buffer首地址， 参数2 数据长度
 * GPIO_input   处理io口输入信号
 **/
typedef struct StateMachine
{
    void (*buffer1DataHandle)(unsigned char *, char);
    void (*buffer2DataHandle)(unsigned char *, char);
    void (*GPIO_input)();
} StateMachine;
#endif
