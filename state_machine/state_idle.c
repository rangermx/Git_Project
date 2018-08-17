#include "state_idle.h"

StateMachine state_idle;

void state_idle_buffer1_data_handle(unsigned char *buf, char len)
{
    // 进行空闲状态下的串口1数据操作
}

void state_idle_buffer2_data_handle(unsigned char *buf, char len)
{
    // 进行空闲状态下的串口2数据操作
}

void state_idle_GPIO_input()
{
    // 进行空闲状态下的GPIO输入操作
}

void state_idle_init()
{
    state_idle.buffer1DataHandle = state_idle_buffer1_data_handle;
    state_idle.buffer2DataHandle = state_idle_buffer2_data_handle;
    state_idle.GPIO_input = state_idle_GPIO_input;
}
