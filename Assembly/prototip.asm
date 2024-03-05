.386
.model flat, stdcall
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;includem biblioteci, si declaram ce functii vrem sa importam
includelib msvcrt.lib
extern exit: proc
extern malloc: proc
extern memset: proc
extern printf: proc

includelib kernel32.lib
extern GetLocalTime@4: proc
includelib canvas.lib
extern BeginDrawing: proc
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;declaram simbolul start ca public - de acolo incepe executia
public start
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;sectiunile programului, date, respectiv cod
.data
;aici declaram date
window_title DB "Ceas digital",0
area_width EQU 640
area_height EQU 480
area DD 0

counter DD 0 ; numara evenimentele de tip timer

arg1 EQU 8
arg2 EQU 12
arg3 EQU 16
arg4 EQU 20

h DD 0
m DD 0
s DD 0

format db "%d", 10, 13, 0

h_alarma DD 0
m_alarma DD 0

x dd 0
y dd 0

mytimeStruct STRUCT
  wYear word ?
  wMonth word ?
  wDayOfWeek word ?
  wDay word ?
  wHour word ?
  wMinute word ?
  wSecond word ?
  wMilliseconds word ?
mytimeStruct ends

mytime mytimeStruct <>

symbol_width EQU 34
symbol_height EQU 66
include digits.inc
include letters.inc

.code
; procedura make_text afiseaza o litera sau o cifra la coordonatele date
; arg1 - simbolul de afisat (litera sau cifra)
; arg2 - pointer la vectorul de pixeli
; arg3 - pos_x
; arg4 - pos_y
make_text proc
	push ebp
	mov ebp, esp
	pusha
	
	mov eax, [ebp+arg1] ; citim simbolul de afisat
	cmp eax, 'A'
	jl make_digit
	cmp eax, 'Z'
	jg make_digit
	sub eax, 'A'
	lea esi, letters
	jmp draw_text
make_digit:
	cmp eax, '0'
	jl make_space
	cmp eax, '9'
	jg make_space
	sub eax, '0'
	lea esi, digits
	jmp draw_text
make_space:	
	mov eax, 26 ; de la 0 pana la 25 sunt litere, 26 e space
	lea esi, letters
draw_text:
	mov ebx, symbol_width
	mul ebx
	mov ebx, symbol_height
	mul ebx
	add esi, eax
	mov ecx, symbol_height
bucla_simbol_linii:
	mov edi, [ebp+arg2] ; pointer la matricea de pixeli
	mov eax, [ebp+arg4] ; pointer la coord y
	add eax, symbol_height
	sub eax, ecx
	mov ebx, area_width
	mul ebx
	add eax, [ebp+arg3] ; pointer la coord x
	shl eax, 2 ; inmultim cu 4, avem un DWORD per pixel
	add edi, eax
	push ecx
	mov ecx, symbol_width
bucla_simbol_coloane:
	cmp byte ptr [esi], 0
	je simbol_pixel_alb
	mov dword ptr [edi], 0
	jmp simbol_pixel_next
simbol_pixel_alb:
	mov dword ptr [edi], 0FFFFFFh
simbol_pixel_next:
	inc esi
	add edi, 4
	loop bucla_simbol_coloane
	pop ecx
	loop bucla_simbol_linii
	popa
	mov esp, ebp
	pop ebp
	ret
make_text endp	

draw_point_macro macro drawArea, x, y
	push eax
	push ebx
	push ecx
	push edx
	
	mov eax, 0
	mov eax, x
	mov ebx, 640
	mul ebx
	add eax, y
	shl eax, 2
	mov ebx, drawArea
	mov dword ptr [ebx+eax], 000000h
	mov dword ptr [ebx+eax+2560], 000000h
	mov dword ptr [ebx+eax+2560+2560], 000000h
	mov dword ptr [ebx+eax+2560+2560+2560], 000000h
	mov dword ptr [ebx+eax+4], 000000h
	mov dword ptr [ebx+eax+4+2560], 000000h
	mov dword ptr [ebx+eax+4+2560+2560], 000000h
	mov dword ptr [ebx+eax+4+2560+2560+2560], 000000h
	mov dword ptr [ebx+eax+8], 000000h
	mov dword ptr [ebx+eax+8+2560], 000000h
	mov dword ptr [ebx+eax+8+2560+2560], 0000ffh
	mov dword ptr [ebx+eax+8+2560+2560+2560], 000000h
	mov dword ptr [ebx+eax+12], 000000h
	mov dword ptr [ebx+eax+12+2560], 000000h
	mov dword ptr [ebx+eax+12+2560+2560], 0000ffh
	mov dword ptr [ebx+eax+12+2560+2560+2560], 000000h

	pop edx
	pop ecx
	pop ebx
	pop eax
endm

draw_vertical_line_macro macro drawArea, x, y, len
LOCAL vf, final
	push eax
	push ecx
	push ebx
	
	mov eax, 0
	mov eax, x
	mov ebx, 640
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	add eax, 2559
	mov EBX, drawArea
	add ebx, ecx
	mov dword ptr [ebx+eax], 000000h
	inc ecx
	inc ecx
	cmp ecx, len
	je final
	loop vf
final:
	pop ebx
	pop ecx
	pop eax
endm

draw_horizontal_line_macro macro drawArea, x, y, len
LOCAL vf, final
	push eax
	push ecx
	push ebx
	
	mov eax, 0
	mov eax, x
	mov ebx, 640
	mul ebx
	add eax, y
	shl eax, 2
	mov ecx, 0
vf:
	mov EBX, drawArea
	add ebx, ecx
	mov dword ptr [ebx+eax], 000000h
	inc ecx
	inc ecx
	cmp ecx, len
	je final
	loop vf
final:
	pop ebx
	pop ecx
	pop eax
endm

; un macro ca sa apelam mai usor desenarea simbolului
make_text_macro macro symbol, drawArea, x, y
	push y
	push x
	push drawArea
	push symbol
	call make_text
	add esp, 16
endm

; functia de desenare - se apeleaza la fiecare click
; sau la fiecare interval de 200ms in care nu s-a dat click
; arg1 - evt (0 - initializare, 1 - click, 2 - s-a scurs intervalul fara click)
; arg2 - x
; arg3 - y
draw proc
	push ebp
	mov ebp, esp
	pusha
	
	mov eax, [ebp+arg1]
	cmp eax, 1
	jz evt_click
	cmp eax, 2
	jz evt_timer ; nu s-a efectuat click pe nimic

	
evt_click:
	mov eax, [ebp+arg2]
	mov x, eax
	mov eax, [ebp+arg3]
	mov y, eax
	cmp y, 325
	jl evt_timer
	cmp y, 375
	jg evt_timer
	cmp x, 150
	jl evt_timer
	cmp x, 180
	jg test_inc_h
	cmp h, 0
	je reset_23_h
	dec h
	jmp evt_timer
reset_23_h:
	mov h, 23
	jmp evt_timer
	
test_inc_h:
	cmp x, 200
	jl evt_timer
	cmp x, 230
	jg test_dec_m
	cmp h, 23
	je reset_0_h
	inc h
	jmp evt_timer
reset_0_h:
	mov h, 0
	jmp evt_timer

test_dec_m:
	cmp x, 250
	jl evt_timer
	cmp x, 280
	jg test_inc_m
	cmp m, 0
	je reset_59_m
	dec m
	jmp evt_timer
reset_59_m:
	mov m, 59
	jmp evt_timer

test_inc_m:
	cmp x, 300
	jl evt_timer
	cmp x, 330
	jg test_dec_s
	cmp m, 59
	je reset_0_m
	inc m
	jmp evt_timer
reset_0_m:
	mov m, 0
	jmp evt_timer
	
test_dec_s:
	cmp x, 350
	jl evt_timer
	cmp x, 380
	jg test_inc_s
	cmp s, 0
	je reset_59_s
	dec s
	jmp evt_timer
reset_59_s:
	mov s, 59
	jmp evt_timer
	
test_inc_s:
	cmp x, 400
	jl evt_timer
	cmp x, 430
	jg evt_timer
	cmp s, 59
	je reset_0_s
	inc s
	jmp evt_timer	
reset_0_s:
	mov s, 0
	jmp evt_timer
	
evt_timer:
	inc counter
	cmp counter,5
	jne afisare_numere
	mov counter,0
	inc s
	cmp s,60
	jne afisare_numere
	mov s,0
	inc m
	cmp m,60
	jne afisare_numere
	inc h
	mov m, 0
	cmp h,24
	jne afisare_numere
	mov h,0

afisare_numere:
	;afisare h, m, s
	;afisare h
	mov ebx, 10
	mov eax, h
	;cifra unitati
	mov edx, 0
	div ebx
	add edx, '0'
	make_text_macro edx, area, 190, 240
	;cifra zeci
	mov edx, 0
	div ebx
	add edx, '0'
	make_text_macro edx, area, 150, 240
	
	;afisare m
	mov ebx, 10
	mov eax, m
	;cifra unitati
	mov edx, 0
	div ebx
	add edx, '0'
	make_text_macro edx, area, 290, 240
	;cifra zeci
	mov edx, 0
	div ebx
	add edx, '0'
	make_text_macro edx, area, 250, 240
	
	;afisare s
	mov ebx, 10
	mov eax, s
	;cifra unitati
	mov edx, 0
	div ebx
	add edx, '0'
	make_text_macro edx, area, 390, 240
	;cifra zeci
	mov edx, 0
	div ebx
	add edx, '0'
	make_text_macro edx, area, 350, 240


final_draw:
	;cadranul
	draw_horizontal_line_macro  area, 230, 140, 1200
	draw_vertical_line_macro area, 230, 140, 85
	draw_horizontal_line_macro area, 315, 140, 1200
	draw_vertical_line_macro area, 230, 440, 85
	
	;separatoare
	draw_point_macro area, 260, 335
	draw_point_macro area, 285, 335
	draw_point_macro area, 260, 235
	draw_point_macro area, 285, 235
	
	
	
	popa
	mov esp, ebp
	pop ebp
	ret
	
draw endp

get_time proc
	push offset mytime
	call GetLocalTime@4
	
	mov ebx, 0
	mov bx, mytime.wHour
	mov h, ebx
	mov ebx, 0
	mov bx, mytime.wMinute
	mov m, ebx
	mov ebx, 0
	mov bx, mytime.wSecond
	mov s, ebx
	ret
get_time endp

start:
	call get_time
	;call make_sound
	;alocam memorie pentru zona de desenat
	mov eax, area_width
	mov ebx, area_height
	mul ebx
	shl eax, 2
	push eax
	call malloc
	add esp, 4
	mov area, eax
	;apelam functia de desenare a ferestrei
	; typedef void (*DrawFunc)(int evt, int x, int y);
	; void __cdecl BeginDrawing(const char *title, int width, int height, unsigned int *area, DrawFunc draw);
	push offset draw
	push area
	push area_height
	push area_width
	push offset window_title
	call BeginDrawing
	add esp, 20
	
	;terminarea programului
	push 0
	call exit
end start