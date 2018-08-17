#include "state_unusual.h"

StateMachine state_unusual;

void state_unusual_buffer1_data_handle(unsigned char *buf, char len)
{
    // 进行异常状态下的串口1数据操作
}

void state_unusual_buffer2_data_handle(unsigned char *buf, char len)
{
    // 进行异常状态下的串口2数据操作
}

void state_unusual_GPIO_input()
{
    // 进行异常状态下的GPIO输入操作
}

void state_unusual_init()
{
    state_unusual.buffer1DataHandle = state_unusual_buffer1_data_handle;
    state_unusual.buffer2DataHandle = state_unusual_buffer2_data_handle;
    state_unusual.GPIO_input = state_unusual_GPIO_input;
}
