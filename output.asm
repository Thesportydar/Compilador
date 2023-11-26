.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
callStack DWORD 100 DUP(?)
stackPointer DWORD 0
@1 dw ?
@2 dw ?
@3 dd ?
@11 dw ?
@14 dw ?
@21 dw ?
@26 dw ?
@27 dd ?
@32 dw ?
@37 dw 10
@38 dw 9
@41 dw 9
@42 db " ES IGUAL ", 0
@43 db " ES DISTINTO ", 0
@47 dw 1
@errorEjecucion db "ERROR EJECUCION", 0
@recursividad db "RECURSIVIDAD DETECTADA", 0
@overflow db "OVERFLOW DETECTADO", 0
.code
fun4@global@class1 proc
mov eax, offset fun4@global@class1
call CheckCallStack
call PushToCallStack
mov ax, @2
mov @2, ax
call fun4@global@class1
call PopFromCallStack
ret
fun4@global@class1 endp
fun1@global@class2 proc
mov eax, offset fun1@global@class2
call CheckCallStack
call PushToCallStack
mov ax, @1
mov @21, ax
call PopFromCallStack
ret
fun1@global@class2 endp
fun2@global@class2 proc
mov eax, offset fun2@global@class2
call CheckCallStack
call PushToCallStack
mov ax, @26
mov @21, ax
call PopFromCallStack
ret
fun2@global@class2 endp
fun1@global proc
mov eax, offset fun1@global
call CheckCallStack
call PushToCallStack
fld @27
fstp @27
call PopFromCallStack
ret
fun1@global endp
CheckCallStack PROC
mov ecx, stackPointer
cmp ecx, 0
je NoRecursion
lea edx, [callStack + ecx * 4 - 4]
cmp DWORD PTR [edx], eax
je Recursion
jmp NoRecursion
Recursion:
invoke MessageBox, NULL, addr @recursividad, addr @errorEjecucion, MB_OK
invoke ExitProcess, 0
NoRecursion:
ret
CheckCallStack endp
PushToCallStack PROC
mov edx, stackPointer
mov DWORD PTR [callStack + edx * 4], eax
inc stackPointer
ret
PushToCallStack endp
PopFromCallStack PROC
dec stackPointer
mov edx, stackPointer
lea edx, [callStack + edx * 4]
mov eax, DWORD PTR [edx]
ret
PopFromCallStack endp
start:
mov ax, @1
mov @1, ax
mov ax, @14
mov @14, ax
mov ax, @1
mov @14, ax
mov ax, @37
mov @32, ax
mov ax, @38
mov @1, ax
mov ax, @1
mov @26, ax
mov ax, @32
mov @21, ax
call fun2@global@class2
mov ax, @26
mov @1, ax
mov ax, @21
mov @32, ax
mov ax, @32
mov @21, ax
call fun1@global@class2
mov ax, @21
mov @32, ax
mov ax, @32
mov @2, ax
call fun4@global@class1
mov ax, @2
mov @32, ax
mov ax, @32
cmp ax, @41
je label0
jmp END_IF0
label0:
invoke MessageBox, NULL, addr @42, addr @42, MB_OK
jmp END_IF1
END_IF0:
invoke MessageBox, NULL, addr @43, addr @43, MB_OK
END_IF1:
fld @3
fstp @27
call fun1@global
fld @27
fstp @3
mov ax, @47
mov @32, ax
invoke ExitProcess, 0
end start
