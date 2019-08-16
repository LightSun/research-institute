
#ifdef __cplusplus
extern "C" {
#endif

#include "clib/luaaux.h"
#include "test/p1_test.h"

int main(int argc, char **argv) {
    LOGD("\n--------------------test case 1--------------------------\n");
    p1_test_result01();
    LOGD("\n--------------------test case 2--------------------------\n");
    p1_test_result02();
    LOGD("\n--------------------test case 3--------------------------\n");
    p1_test_result03();
    LOGD("\n--------------------test case 4--------------------------\n");
    p1_test_result04();
    LOGD("\n--------------------test case 5--------------------------\n");
    p1_test_result05();
    LOGD("\n--------------------test case 6--------------------------\n");
    p1_test_result06();
    LOGD("\n--------------------test case 7--------------------------\n");
    p1_test_result07();
    LOGD("\n--------------------test case 8--------------------------\n");
    p1_test_result08();
    LOGD("\n--------------------test case 9--------------------------\n");
    p1_test_result09();
    LOGD("\n--------------------test case 10-------------------------\n");
    p1_test_result10();
    LOGD("\n--------------------test case 11-------------------------\n");
    p1_test_nestcall01();

    return 0;
}
#ifdef __cplusplus
}
#endif
