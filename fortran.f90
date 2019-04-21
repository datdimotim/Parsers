module tables
implicit none
integer*1,parameter::max_deep_perebor=30
integer*1,parameter::orange_gran=6
integer*1,parameter::yellow_gran=5
integer*1,parameter::  blue_gran=4
integer*1,parameter:: green_gran=3
integer*1,parameter:: white_gran=2
integer*1,parameter::   red_gran=1
integer*1,parameter::  napravlenie_plus=1
integer*1,parameter::napravlenie_double=2
integer*1,parameter:: napravlenie_minus=3
integer*1,parameter::      red_gran_plus=1
integer*1,parameter::     red_gran_minus=2
integer*1,parameter::    red_gran_double=3
integer*1,parameter::    white_gran_plus=4
integer*1,parameter::   white_gran_minus=5
integer*1,parameter::  white_gran_double=6
integer*1,parameter::    green_gran_plus=7
integer*1,parameter::   green_gran_minus=8
integer*1,parameter::  green_gran_double=9
integer*1,parameter::    blue_gran_plus=10
integer*1,parameter::   blue_gran_minus=11
integer*1,parameter::  blue_gran_double=12
integer*1,parameter::  yellow_gran_plus=13
integer*1,parameter:: yellow_gran_minus=14
integer*1,parameter::yellow_gran_double=15
integer*1,parameter::  orange_gran_plus=16
integer*1,parameter:: orange_gran_minus=17
integer*1,parameter::orange_gran_double=18
integer*2,parameter::x1_max=2187-1
integer*2,parameter::y1_max=2048-1
integer*2,parameter::z1_max=495-1
integer*4,parameter::x2_max=40320-1
integer*4,parameter::y2_max=40320-1
integer*1,parameter::z2_max=24-1
integer*1,parameter::x1_max_hods=6
integer*1,parameter::y1_max_hods=7
integer*1,parameter::z1_max_hods=5
integer*1,parameter::x2_max_hods=13
integer*1,parameter::y2_max_hods=8
integer*1,parameter::z2_max_hods=4
integer*1,parameter::xy1_max_hods=9
integer*1,parameter::xz1_max_hods=9
integer*1,parameter::yz1_max_hods=9
integer*1,parameter::xz2_max_hods=14
integer*1,parameter::yz2_max_hods=12
integer*1,parameter::povorots_2(0:10)=(/0,1,2,3,6,9,12,15,16,17,18/)

integer*1::povorot_on_facelet_table(0:18,54);pointer(povorot_on_facelet_table_p,povorot_on_facelet_table)
integer*1::hod_po_predhod(0:18,0:18)        ;pointer(hod_po_predhod_p,hod_po_predhod)
integer*2::x1_move(0:18,0:x1_max)           ;pointer(x1_move_p,x1_move)
integer*2::y1_move(0:18,0:x1_max)           ;pointer(y1_move_p,y1_move)
integer*2::z1_move(0:18,0:z1_max)           ;pointer(z1_move_p,z1_move)
integer*4::x2_move(0:10,0:x2_max)           ;pointer(x2_move_p,x2_move)
integer*4::y2_move(0:10,0:x2_max)           ;pointer(y2_move_p,y2_move)
integer*1::z2_move(0:10,0:z2_max)           ;pointer(z2_move_p,z2_move)
integer*1::x1_deep(0:x1_max)                ;pointer(x1_deep_p,x1_deep)
integer*1::y1_deep(0:y1_max)                ;pointer(y1_deep_p,y1_deep)
integer*1::z1_deep(0:z1_max)                ;pointer(z1_deep_p,z1_deep)
integer*1::z2_deep(0:z2_max)                ;pointer(z2_deep_p,z2_deep)
integer*1::x2_deep(0:x2_max)                ;pointer(x2_deep_p,x2_deep)
integer*1::y2_deep(0:y2_max)                ;pointer(y2_deep_p,y2_deep)
integer*1::xy1_deep(0:x1_max,0:y1_max)      ;pointer(xy1_deep_p,xy1_deep)
integer*1::xz1_deep(0:x1_max,0:z1_max)      ;pointer(xz1_deep_p,xz1_deep)
integer*1::yz1_deep(0:y1_max,0:z1_max)      ;pointer(yz1_deep_p,yz1_deep)
integer*1::xz2_deep(0:x2_max,0:z2_max)      ;pointer(xz2_deep_p,xz2_deep)
integer*1::yz2_deep(0:y2_max,0:z2_max)      ;pointer(yz2_deep_p,yz2_deep)

integer*1::tablebase_massiv(sizeof(povorot_on_facelet_table)+sizeof(hod_po_predhod)+sizeof(x1_move)+sizeof(y1_move)+sizeof(z1_move)+sizeof(x2_move)+sizeof(y2_move)+sizeof(z2_move)&
+sizeof(x1_deep)+sizeof(y1_deep)+sizeof(z1_deep)+sizeof(x2_deep)+sizeof(y2_deep)+sizeof(z2_deep)+sizeof(xy1_deep)+sizeof(xz1_deep)+sizeof(yz1_deep)+sizeof(xz2_deep)+sizeof(yz2_deep))
pointer(tablebase_massiv_p,tablebase_massiv)

integer*1,parameter::ugol_color_tabl(3,8)=(/  orange_gran,green_gran,yellow_gran,&
                                              orange_gran,yellow_gran,blue_gran,&
                                              orange_gran,blue_gran,white_gran,&
                                              orange_gran,white_gran,green_gran,&
                                            !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                              red_gran,yellow_gran,green_gran,&
                                              red_gran,blue_gran,yellow_gran,&
                                              red_gran,white_gran,blue_gran,&
                                              red_gran,green_gran,white_gran/)
integer*1,parameter::ugol_place_tabl(3,8)=(/  46,21,37,&
                                              48,39,30,&
                                              54,28,12,&
                                              52,10,19,&!!!!!!!
                                              1,43,27,&
                                              3,36,45,&
                                              9,18,34,&
                                              7,25,16/)
integer*1,parameter::rebro_color_tabl(2,12)=(/orange_gran,yellow_gran,&
                                              orange_gran,blue_gran,&
                                              orange_gran,white_gran,&
                                              orange_gran,green_gran,&
                                            !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                              yellow_gran,green_gran,&
                                              yellow_gran,blue_gran,&
                                              white_gran,blue_gran,&
                                              white_gran,green_gran,&
                                            !!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                              red_gran,yellow_gran,&
                                              red_gran,blue_gran,&
                                              red_gran,white_gran,&
                                              red_gran,green_gran/)
integer*1,parameter::rebro_place_tabl(2,12)=(/47,38,&
                                              51,29,&
                                              53,11,&
                                              49,20,&!!!!!
                                              40,24,&
                                              42,33,&
                                              15,31,&
                                              13,22,&!!!!!
                                              2,44,&
                                              6,35,&
                                              8,17,&
                                              4,26/)
contains
subroutine create_tables
call init_pointers
call init_povorot_on_facelet_table
call create_hod_table
call create_movetable_x1
call create_deeptable_x1
call create_movetable_y1
call create_deeptable_y1
call create_movetable_x2
call create_deeptable_x2
call create_movetable_y2
call create_deeptable_y2
call create_movetable_z1
call create_deeptable_z1
call create_movetable_z2
call create_deeptable_z2
call create_deeptable_xy1
call create_deeptable_xz1
call create_deeptable_yz1
call create_deeptable_xz2
call create_deeptable_yz2
end subroutine create_tables

subroutine tables_to_file
open(1,file='tablebase.bin',form='binary')
write(1)povorot_on_facelet_table
write(1)hod_po_predhod
write(1)x1_move
write(1)y1_move
write(1)z1_move
write(1)x2_move
write(1)y2_move
write(1)z2_move
write(1)x1_deep
write(1)y1_deep
write(1)z1_deep
write(1)z2_deep
write(1)x2_deep
write(1)y2_deep
write(1)xy1_deep
write(1)xz1_deep
write(1)yz1_deep
write(1)xz2_deep
write(1)yz2_deep
close(1)
end subroutine tables_to_file

subroutine tables_in_file
call init_pointers
open(1,file='tablebase.bin',form='binary')
read(1)povorot_on_facelet_table
read(1)hod_po_predhod
read(1)x1_move
read(1)y1_move
read(1)z1_move
read(1)x2_move
read(1)y2_move
read(1)z2_move
read(1)x1_deep
read(1)y1_deep
read(1)z1_deep
read(1)z2_deep
read(1)x2_deep
read(1)y2_deep
read(1)xy1_deep
read(1)xz1_deep
read(1)yz1_deep
read(1)xz2_deep
read(1)yz2_deep
close(1)
end subroutine tables_in_file

subroutine init_pointers
implicit none
integer*4::sdvig
sdvig=1
povorot_on_facelet_table_p=loc(tablebase_massiv(sdvig))     ;sdvig=sdvig+sizeof(povorot_on_facelet_table)
hod_po_predhod_p=loc(tablebase_massiv(sdvig))               ;sdvig=sdvig+sizeof(hod_po_predhod)
x1_move_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(x1_move)
y1_move_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(y1_move)
z1_move_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(z1_move)
x2_move_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(x2_move)
y2_move_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(y2_move)
z2_move_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(z2_move)
x1_deep_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(x1_deep)
y1_deep_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(y1_deep)
z1_deep_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(z1_deep)
z2_deep_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(z2_deep)
x2_deep_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(x2_deep)
y2_deep_p=loc(tablebase_massiv(sdvig))                      ;sdvig=sdvig+sizeof(y2_deep)
xy1_deep_p=loc(tablebase_massiv(sdvig))                     ;sdvig=sdvig+sizeof(xy1_deep)
xz1_deep_p=loc(tablebase_massiv(sdvig))                     ;sdvig=sdvig+sizeof(xz1_deep)
yz1_deep_p=loc(tablebase_massiv(sdvig))                     ;sdvig=sdvig+sizeof(yz1_deep)
xz2_deep_p=loc(tablebase_massiv(sdvig))                     ;sdvig=sdvig+sizeof(xz2_deep)
yz2_deep_p=loc(tablebase_massiv(sdvig))                     ;sdvig=sdvig+sizeof(yz2_deep)
end subroutine init_pointers

subroutine init_povorot_on_facelet_table
implicit none
integer*1::napravlenie,n_povorot,grani(6,3,3),i,x,y
do i=1,54
povorot_on_facelet_table(0,i)=i
enddo
do n_povorot=1,6
do napravlenie=1,3
    do i=1,6
    do y=1,3
    do x=1,3
        grani(i,y,x)=(i-1)*9+(y-1)*3+x
    enddo
    enddo
    enddo
    call povorot_on_facelet(grani,n_povorot,napravlenie)
    do i=1,6
    do y=1,3
    do x=1,3
        if(napravlenie.eq.1)povorot_on_facelet_table((n_povorot-1)*3+1,(i-1)*9+(y-1)*3+x)=grani(i,y,x)
        if(napravlenie.eq.2)povorot_on_facelet_table((n_povorot-1)*3+3,(i-1)*9+(y-1)*3+x)=grani(i,y,x)
        if(napravlenie.eq.3)povorot_on_facelet_table((n_povorot-1)*3+2,(i-1)*9+(y-1)*3+x)=grani(i,y,x)
    enddo
    enddo
    enddo
enddo
enddo
end subroutine init_povorot_on_facelet_table

subroutine create_movetable_x1
implicit none
integer*2::i,pos
integer*1::np,ugol(8),ugol_p(8)=(/1,2,3,4,5,6,7,8/),facelet(54),facelet_tmp(54)
x1_move(0:18,0:x1_max)=0
do i=0,x1_max
x1_move(0,i)=i
enddo

do i=0,x1_max
do np=1,18
    call ugol_unpack_orientation(ugol,i)
    call ugol_to_facelet(ugol_p,ugol,facelet)
    facelet_tmp(:)=facelet(povorot_on_facelet_table(np,:))
    call facelet_to_ugol_table_orientation(facelet_tmp,ugol)
    call ugol_pack_orientation(ugol,pos)
    x1_move(np,i)=pos
enddo
enddo
end subroutine create_movetable_x1

subroutine create_deeptable_x1
implicit none
integer*2::i
integer*1::np,deep
x1_deep=20
x1_deep(0)=0
do deep=0,20
do i=0,x1_max
    if(x1_deep(i).eq.deep)then
        do np=1,18
        if(x1_deep(x1_move(np,i)).gt.deep+1)x1_deep(x1_move(np,i))=deep+1
        enddo
    endif
enddo
enddo
end subroutine create_deeptable_x1

subroutine create_movetable_y1
implicit none
integer*2::i,pos
integer*1::np,rebro(12),rebro_p(12)=(/1,2,3,4,5,6,7,8,9,10,11,12/),facelet(54),facelet_tmp(54)
y1_move(0:18,0:y1_max)=0
do i=0,y1_max
y1_move(0,i)=i
enddo

do i=0,y1_max
do np=1,18
    call rebro_unpack_orientation(rebro,i)
    call rebro_to_facelet(rebro_p,rebro,facelet)
    facelet_tmp(:)=facelet(povorot_on_facelet_table(np,:))
    call facelet_to_rebro_table_orientation(facelet_tmp,rebro)
    call rebro_pack_orientation(rebro,pos)
    y1_move(np,i)=pos
enddo
enddo
end subroutine create_movetable_y1

subroutine create_deeptable_y1
implicit none
integer*2::i
integer*1::np,deep
y1_deep=20
y1_deep(0)=0
do deep=0,20
do i=0,y1_max
    if(y1_deep(i).eq.deep)then
        do np=1,18
        if(y1_deep(y1_move(np,i)).gt.deep+1)y1_deep(y1_move(np,i))=deep+1
        enddo
    endif
enddo
enddo
end subroutine create_deeptable_y1

subroutine create_movetable_x2
implicit none
integer*4::i,pos
integer*1::np,ugol(8)=0,ugol_p(8),facelet(54),facelet_tmp(54)
x2_move(0:10,0:x2_max)=0
do i=0,x2_max
x2_move(0,i)=i
enddo

do i=0,x2_max
do np=1,10
    call ugol_unpack_perestanovka(ugol_p,i)
    call ugol_to_facelet(ugol_p,ugol,facelet)
    facelet_tmp(:)=facelet(povorot_on_facelet_table(povorots_2(np),:))
    call facelet_to_ugol_table_perestanovka(facelet_tmp,ugol_p)
    call ugol_pack_perestanovka(ugol_p,pos)
    x2_move(np,i)=pos
enddo
enddo
end subroutine create_movetable_x2

subroutine create_deeptable_x2
implicit none
integer*4::i
integer*1::np,deep
x2_deep=20
x2_deep(0)=0
do deep=0,20
do i=0,x2_max
    if(x2_deep(i).eq.deep)then
        do np=1,10
        if(x2_deep(x2_move(np,i)).gt.deep+1)x2_deep(x2_move(np,i))=deep+1
        enddo
    endif
enddo
enddo
end subroutine create_deeptable_x2

subroutine create_movetable_y2
implicit none
integer*4::i,pos
integer*1::np,rebro(12)=0,rebro_p(12),facelet(54),facelet_tmp(54)
y2_move(0:10,0:y2_max)=0
do i=0,y2_max
y2_move(0,i)=i
enddo

do i=0,y2_max
do np=1,10
    call rebro_unpack_perestanovka(rebro_p,i)
    call rebro_to_facelet(rebro_p,rebro,facelet)
    facelet_tmp(:)=facelet(povorot_on_facelet_table(povorots_2(np),:))
    call facelet_to_rebro_table_perestanovka(facelet_tmp,rebro_p)
    call rebro_pack_perestanovka(rebro_p,pos)
    y2_move(np,i)=pos
enddo
enddo
end subroutine create_movetable_y2

subroutine create_deeptable_y2
implicit none
integer*4::i
integer*1::np,deep
y2_deep=20
y2_deep(0)=0
do deep=0,20
do i=0,y2_max
    if(y2_deep(i).eq.deep)then
        do np=1,10
        if(y2_deep(y2_move(np,i)).gt.deep+1)y2_deep(y2_move(np,i))=deep+1
        enddo
    endif
enddo
enddo
end subroutine create_deeptable_y2

subroutine create_movetable_z1
implicit none
integer*2::i,pos
integer*1::np,rebro(12)=0,rebro_p(12),facelet(54),facelet_tmp(54)
z1_move(0:18,0:z1_max)=0
do i=0,z1_max
z1_move(0,i)=i
enddo

do i=0,z1_max
do np=1,18
    call rebro_unpack_z1(rebro_p,i)
    call rebro_to_facelet(rebro_p,rebro,facelet)
    facelet_tmp(:)=facelet(povorot_on_facelet_table(np,:))
    call facelet_to_rebro_table_perestanovka(facelet_tmp,rebro_p)
    call rebro_pack_z1(rebro_p,pos)
    z1_move(np,i)=pos
enddo
enddo
end subroutine create_movetable_z1

subroutine create_deeptable_z1
implicit none
integer*2::i
integer*1::np,deep
z1_deep=20
z1_deep(0)=0
do deep=0,20
do i=0,z1_max
    if(z1_deep(i).eq.deep)then
        do np=1,18
        if(z1_deep(z1_move(np,i)).gt.deep+1)z1_deep(z1_move(np,i))=deep+1
        enddo
    endif
enddo
enddo
end subroutine create_deeptable_z1

subroutine create_movetable_z2
implicit none
integer*1::i,pos
integer*1::np,rebro(12)=0,rebro_p(12),facelet(54),facelet_tmp(54)
z2_move(0:10,0:z2_max)=0
do i=0,z2_max
z2_move(0,i)=i
enddo

do i=0,z2_max
do np=1,10
    call rebro_unpack_z2(rebro_p,i)
    call rebro_to_facelet(rebro_p,rebro,facelet)
    facelet_tmp(:)=facelet(povorot_on_facelet_table(povorots_2(np),:))
    call facelet_to_rebro_table_perestanovka(facelet_tmp,rebro_p)
    call rebro_pack_z2(rebro_p,pos)
    z2_move(np,i)=pos
enddo
enddo
end subroutine create_movetable_z2

subroutine create_deeptable_z2
implicit none
integer*4::i
integer*1::np,deep
z2_deep=20
z2_deep(0)=0
do deep=0,20
do i=0,z2_max
    if(z2_deep(i).eq.deep)then
        do np=1,10
        if(z2_deep(z2_move(np,i)).gt.deep+1)z2_deep(z2_move(np,i))=deep+1
        enddo
    endif
enddo
enddo
end subroutine create_deeptable_z2

subroutine create_deeptable_xy1
implicit none
integer*2::i,j
integer*1::np,deep
xy1_deep=20
xy1_deep(0,0)=0
do deep=0,20
    do i=0,x1_max
        do j=0,y1_max
            if(xy1_deep(i,j).eq.deep)then
                do np=1,18
                if(xy1_deep(x1_move(np,i),y1_move(np,j)).gt.deep+1)xy1_deep(x1_move(np,i),y1_move(np,j))=deep+1
                enddo
            endif
        enddo
    enddo
enddo
end subroutine create_deeptable_xy1

subroutine create_deeptable_xz1
implicit none
integer*2::i,j
integer*1::np,deep
xz1_deep=20
xz1_deep(0,0)=0
do deep=0,20
    do i=0,x1_max
        do j=0,z1_max
            if(xz1_deep(i,j).eq.deep)then
                do np=1,18
                if(xz1_deep(x1_move(np,i),z1_move(np,j)).gt.deep+1)xz1_deep(x1_move(np,i),z1_move(np,j))=deep+1
                enddo
            endif
        enddo
    enddo
enddo
end subroutine create_deeptable_xz1

subroutine create_deeptable_yz1
implicit none
integer*2::i,j
integer*1::np,deep
yz1_deep=20
yz1_deep(0,0)=0
do deep=0,20
    do i=0,y1_max
        do j=0,z1_max
            if(yz1_deep(i,j).eq.deep)then
                do np=1,18
                if(yz1_deep(y1_move(np,i),z1_move(np,j)).gt.deep+1)yz1_deep(y1_move(np,i),z1_move(np,j))=deep+1
                enddo
            endif
        enddo
    enddo
enddo
end subroutine create_deeptable_yz1

subroutine create_deeptable_xz2
implicit none
integer*4::i,j
integer*1::np,deep
xz2_deep=20
xz2_deep(0,0)=0
do deep=0,20
    do i=0,x2_max
        do j=0,z2_max
            if(xz2_deep(i,j).eq.deep)then
                do np=1,10
                if(xz2_deep(x2_move(np,i),z2_move(np,j)).gt.deep+1)xz2_deep(x2_move(np,i),z2_move(np,j))=deep+1
                enddo
            endif
        enddo
    enddo
enddo
end subroutine create_deeptable_xz2

subroutine create_deeptable_yz2
implicit none
integer*4::i,j
integer*1::np,deep
yz2_deep=20
yz2_deep(0,0)=0
do deep=0,20
    do i=0,y2_max
        do j=0,z2_max
            if(yz2_deep(i,j).eq.deep)then
                do np=1,10
                if(yz2_deep(y2_move(np,i),z2_move(np,j)).gt.deep+1)yz2_deep(y2_move(np,i),z2_move(np,j))=deep+1
                enddo
            endif
        enddo
    enddo
enddo
end subroutine create_deeptable_yz2

subroutine create_hod_table
implicit none
integer*1::pred_hod,hod
hod_po_predhod=0
do pred_hod=0,18
    do hod=0,18
        if(pred_hod.ne.0.and.hod.eq.0)cycle
        if(pred_hod.ne.0)then
            if((pred_hod-1)/3.eq.(hod-1)/3)cycle
            if((pred_hod-1)/3.eq.0.and.(hod-1)/3.eq.5)cycle
            if((pred_hod-1)/3.eq.1.and.(hod-1)/3.eq.4)cycle
            if((pred_hod-1)/3.eq.2.and.(hod-1)/3.eq.3)cycle
        endif

    hod_po_predhod(pred_hod,hod)=1
    enddo
enddo
end subroutine create_hod_table
end module tables

!                    \EA\EE\ED\E2\E5\F0\F2\E0\F6\E8\FF \EB\E8\F6\E5\E2\FB\F5 \ED\E0\EA\EB\E5\E5\EA \E2 \EF\EE\EB\EE\E6\E5\ED\E8\E5 \EA\F3\E1\E8\EA\EE\E2(\EE\E4\ED\EE\E7\ED\E0\F7\ED\EE\E5)
subroutine grani_to_facelet(grani,facelet)
implicit none
integer*1,intent(in)::grani(6,3,3)
integer*1,intent(out)::facelet(54)
integer*1::i,y,x
do i=1,6
do y=1,3
do x=1,3
    facelet((i-1)*9+(y-1)*3+x)=grani(i,y,x)
enddo
enddo
enddo
end subroutine grani_to_facelet

subroutine facelet_to_grani(grani,facelet)
implicit none
integer*1,intent(out)::grani(6,3,3)
integer*1,intent(in)::facelet(54)
integer*1::i,y,x
do i=1,6
do y=1,3
do x=1,3
    grani(i,y,x)=facelet((i-1)*9+(y-1)*3+x)
enddo
enddo
enddo
end subroutine facelet_to_grani

subroutine facelet_to_ugol_table_orientation(facelet,ugol)
use tables
implicit none
integer*1,intent(in)::facelet(54)
integer*1,intent(out)::ugol(8)
integer*1::i,orient
do i=1,8
    do orient=1,3
        if(facelet(ugol_place_tabl(orient,i)).eq.red_gran.or.facelet(ugol_place_tabl(orient,i)).eq.orange_gran)ugol(i)=orient-1
    enddo
enddo
end subroutine facelet_to_ugol_table_orientation

subroutine facelet_to_ugol_table_perestanovka(facelet,ugol)
use tables
implicit none
integer*1,intent(in)::facelet(54)
integer*1,intent(out)::ugol(8)
integer*1::i,colors(3),n_kubik,ind_color_kubik,ind_color_in_colors

do i=1,8             !!!!! \E2\FB\E1\EE\F0 \F0\E0\F1\EF\EE\EB\EE\E6\E5\ED\E8\FF \EA\F3\E1\E8\EA\E0, \F1\F7\E8\F2\FB\E2\E0\ED\E8\E5 \ED\E0 \ED\E5\EC \F6\E2\E5\F2\EE\E2
colors(:)=facelet(ugol_place_tabl(:,i))
    do n_kubik=1,8   !!!!!!!!! \EE\EF\F0\E5\E4\E5\EB\E5\ED\E8\E5 \EA\E0\EA\EE\E9 \EA\F3\E1\E8\EA \ED\E0 \E4\E0\ED\ED\EE\EC \EC\E5\F1\F2\E5
        do ind_color_kubik=1,3
            do ind_color_in_colors=1,3
            if(colors(ind_color_in_colors).eq.ugol_color_tabl(ind_color_kubik,n_kubik))exit  !!!! \E5\F1\EB\E8 \ED\E5 \F1\F0\E0\E1\EE\F2\E0\E5\F2 exit, \F2\EE ind_color_in_colors \E1\F3\E4\E5\F2 \F0\E0\E2\E5\ED 4,
            enddo                                                                            !!!! \FD\F2\EE \EE\E7\ED\E0\F7\E0\E5\F2 \F7\F2\EE \E4\E0\ED\ED\EE\E3\EE \F6\E2\E5\F2\E0 \ED\E5\F2  \E2 \EF\F0\E5\E4\EF\EE\EB\E0\E3\E0\E5\EC\EE\EC \EA\F3\E1\E8\EA\E5
            if(ind_color_in_colors.eq.4)exit                                                 !!!! \E5\F1\EB\E8 \F1\F0\E0\E1\EE\F2\E0\E5\F2 \FD\F2\EE\F2 if \F2\EE \E7\ED\E0\F7\E8\F2 \E4\E0\ED\ED\FB\E9 N_kubik \ED\E5 \EF\EE\E4\F5\EE\E4\E8\F2
        enddo
        if(ind_color_kubik.eq.4)ugol(i)=n_kubik                                              !!!! \E5\F1\EB\E8 \ED\E5 \F1\F0\E0\E1\EE\F2\E0\EB \EF\F0\E5\E4\FB\E4\F3\F9\E8\E9 exit \E7\ED\E0\F7\E8\F2 \E2\F1\E5 \F6\E2\E5\F2\E0 \EF\EE\E4\EE\F8\EB\E8 \E8 \EA\F3\E1\E8\EA = n_kubik
    enddo
enddo
end subroutine facelet_to_ugol_table_perestanovka

subroutine facelet_to_rebro_table_orientation(facelet,rebro)
use tables
implicit none
integer*1,intent(in)::facelet(54)
integer*1,intent(out)::rebro(12)
integer*1::orient,i
rebro=-1
do i=1,12
    do orient=1,2
        if(facelet(rebro_place_tabl(orient,i)).eq.red_gran.or.facelet(rebro_place_tabl(orient,i)).eq.orange_gran)rebro(i)=orient-1
    enddo
    if(rebro(i).eq.-1)then
        if(facelet(rebro_place_tabl(1,i)).eq.yellow_gran.or.facelet(rebro_place_tabl(1,i)).eq.white_gran)rebro(i)=0
        if(facelet(rebro_place_tabl(1,i)).eq.green_gran.or.facelet(rebro_place_tabl(1,i)).eq.blue_gran)rebro(i)=1
    endif
enddo
end subroutine facelet_to_rebro_table_orientation

subroutine facelet_to_rebro_table_perestanovka(facelet,rebro)
use tables
implicit none
integer*1,intent(in)::facelet(54)
integer*1,intent(out)::rebro(12)
integer*1::i,colors(2),n_kubik,ind_color_kubik,ind_color_in_colors

do i=1,12             !!!!! \E2\FB\E1\EE\F0 \F0\E0\F1\EF\EE\EB\EE\E6\E5\ED\E8\FF \EA\F3\E1\E8\EA\E0, \F1\F7\E8\F2\FB\E2\E0\ED\E8\E5 \ED\E0 \ED\E5\EC \F6\E2\E5\F2\EE\E2
colors(:)=facelet(rebro_place_tabl(:,i))
    do n_kubik=1,12   !!!!!!!!! \EE\EF\F0\E5\E4\E5\EB\E5\ED\E8\E5 \EA\E0\EA\EE\E9 \EA\F3\E1\E8\EA \ED\E0 \E4\E0\ED\ED\EE\EC \EC\E5\F1\F2\E5
        do ind_color_kubik=1,2
            do ind_color_in_colors=1,2
            if(colors(ind_color_in_colors).eq.rebro_color_tabl(ind_color_kubik,n_kubik))exit  !!!! \E5\F1\EB\E8 \ED\E5 \F1\F0\E0\E1\EE\F2\E0\E5\F2 exit, \F2\EE ind_color_in_colors \E1\F3\E4\E5\F2 \F0\E0\E2\E5\ED 3,
            enddo                                                                            !!!! \FD\F2\EE \EE\E7\ED\E0\F7\E0\E5\F2 \F7\F2\EE \E4\E0\ED\ED\EE\E3\EE \F6\E2\E5\F2\E0 \ED\E5\F2  \E2 \EF\F0\E5\E4\EF\EE\EB\E0\E3\E0\E5\EC\EE\EC \EA\F3\E1\E8\EA\E5
            if(ind_color_in_colors.eq.3)exit                                                 !!!! \E5\F1\EB\E8 \F1\F0\E0\E1\EE\F2\E0\E5\F2 \FD\F2\EE\F2 if \F2\EE \E7\ED\E0\F7\E8\F2 \E4\E0\ED\ED\FB\E9 N_kubik \ED\E5 \EF\EE\E4\F5\EE\E4\E8\F2
        enddo
        if(ind_color_kubik.eq.3)rebro(i)=n_kubik                                              !!!! \E5\F1\EB\E8 \ED\E5 \F1\F0\E0\E1\EE\F2\E0\EB \EF\F0\E5\E4\FB\E4\F3\F9\E8\E9 exit \E7\ED\E0\F7\E8\F2 \E2\F1\E5 \F6\E2\E5\F2\E0 \EF\EE\E4\EE\F8\EB\E8 \E8 \EA\F3\E1\E8\EA = n_kubik
    enddo
enddo
end subroutine facelet_to_rebro_table_perestanovka

subroutine ugol_to_facelet(ugol_p,ugol_o,facelet)
use tables
implicit none
integer*1,intent(in)::ugol_p(8),ugol_o(8)
integer*1,intent(out)::facelet(54)
integer*1::i,j,colors_poradok_orientation_massiv_tmp(3,8),colors_poradok_orientation_massiv(3,8)!!!!!!!!!!! \E7\ED\E0\F7\E5\ED\E8\FF--\F6\E2\E5\F2\E0,\EF\E5\F0\E2\FB\E9 \E8\ED\E4\E5\EA\F1--\F6\E2\E5\F2\E0 \E2 \EF\EE\F0\FF\E4\EA\E5 \EE\F0\E8\E5\ED\F2\E0\F6\E8\E8,\E2\F2\EE\F0\EE\E9 \E8\ED\E4\E5\EA\F1,\F6\E2\E5\F2\E0--\E2 \EF\EE\F0\FF\E4\EA\E5 \ED\EE\EC\E5\F0\EE\E2 \EA\F3\E1\E8\EA\EE\E2
integer*1::op
do i=1,8
    colors_poradok_orientation_massiv(:,i)=ugol_color_tabl(:,ugol_p(i)) !! \EF\E5\F0\E5\F1\F2\E0\E2\EB\FF\E5\EC \EA\F3\E1\E8\EA\E8 \E8\E7 \E5\F1\F2\E5\F1\F1\E2\E2\E5\ED\ED\EE\E3\EE \F0\E0\F1\EF\EE\EB\EE\E6\E5\ED\E8\FF
enddo
do i=1,8
do j=1,3
    op=j+ugol_o(i)
    if(op.gt.3)op=op-3
    colors_poradok_orientation_massiv_tmp(op,i)=colors_poradok_orientation_massiv(j,i)  !! \EF\EE\E2\EE\F0\E0\F7\E8\E2\E0\E5\EC \EF\E5\F0\E5\F1\F2\E0\E2\EB\E5\ED\ED\FB\E5 \EA\F3\E1\E8\EA\E8
enddo
enddo
do i=1,8
do j=1,3
    facelet(ugol_place_tabl(j,i))=colors_poradok_orientation_massiv_tmp(j,i)
enddo
enddo
end subroutine ugol_to_facelet

subroutine rebro_to_facelet(rebro_p,rebro_o,facelet)
use tables
implicit none
integer*1,intent(in)::rebro_p(12),rebro_o(12)
integer*1,intent(out)::facelet(54)
integer*1::i,j,colors_poradok_orientation_massiv_tmp(2,12),colors_poradok_orientation_massiv(2,12)!!!!!!!!!!! \E7\ED\E0\F7\E5\ED\E8\FF--\F6\E2\E5\F2\E0,\EF\E5\F0\E2\FB\E9 \E8\ED\E4\E5\EA\F1--\F6\E2\E5\F2\E0 \E2 \EF\EE\F0\FF\E4\EA\E5 \EE\F0\E8\E5\ED\F2\E0\F6\E8\E8,\E2\F2\EE\F0\EE\E9 \E8\ED\E4\E5\EA\F1,\F6\E2\E5\F2\E0--\E2 \EF\EE\F0\FF\E4\EA\E5 \ED\EE\EC\E5\F0\EE\E2 \EA\F3\E1\E8\EA\EE\E2
integer*1::op
do i=1,12
    colors_poradok_orientation_massiv(:,i)=rebro_color_tabl(:,rebro_p(i)) !! \EF\E5\F0\E5\F1\F2\E0\E2\EB\FF\E5\EC \EA\F3\E1\E8\EA\E8 \E8\E7 \E5\F1\F2\E5\F1\F1\E2\E2\E5\ED\ED\EE\E3\EE \F0\E0\F1\EF\EE\EB\EE\E6\E5\ED\E8\FF
enddo
do i=1,12
do j=1,2
    op=j+rebro_o(i)
    if(op.gt.2)op=op-2
    colors_poradok_orientation_massiv_tmp(op,i)=colors_poradok_orientation_massiv(j,i)  !! \EF\EE\E2\EE\F0\E0\F7\E8\E2\E0\E5\EC \EF\E5\F0\E5\F1\F2\E0\E2\EB\E5\ED\ED\FB\E5 \EA\F3\E1\E8\EA\E8
enddo
enddo
do i=1,12
do j=1,2
    facelet(rebro_place_tabl(j,i))=colors_poradok_orientation_massiv_tmp(j,i)
enddo
enddo
end subroutine rebro_to_facelet

subroutine posl_povorot_in_facelet(facelet,facelet_2_fase,hods)
use tables
implicit none
integer*1,intent(in)::hods(max_deep_perebor),facelet(54)
integer*1,intent(out)::facelet_2_fase(54)
integer*1::i,facelet_tmp(54)
facelet_2_fase=facelet
do i=1,max_deep_perebor
    if(hods(i).eq.0)cycle
    facelet_tmp(:)=facelet_2_fase(povorot_on_facelet_table(hods(i),:))
    facelet_2_fase=facelet_tmp
enddo
end subroutine posl_povorot_in_facelet

subroutine povorot_on_facelet(grani,povorot,napravlenie)
use tables
implicit none
integer*1,intent(inout)::grani(6,3,3)
integer*1,intent(in)::povorot,napravlenie
integer*1::grani_tmp(6,3,3),i
select case(povorot)
case(red_gran)
do i=1,4-napravlenie
    grani_tmp=grani
    grani_tmp(red_gran,1,:)=grani(red_gran,3:1:-1,1)
    grani_tmp(red_gran,2,:)=grani(red_gran,3:1:-1,2)
    grani_tmp(red_gran,3,:)=grani(red_gran,3:1:-1,3)
    grani_tmp(white_gran,3,:)=grani(blue_gran,3,:)
    grani_tmp(blue_gran,3,:)=grani(yellow_gran,3,3:1:-1)
    grani_tmp(yellow_gran,3,:)=grani(green_gran,3,:)
    grani_tmp(green_gran,3,:)=grani(white_gran,3,3:1:-1)
    grani=grani_tmp
enddo
case(white_gran)
do i=1,napravlenie
    grani_tmp=grani
    grani_tmp(white_gran,1,:)=grani(white_gran,3:1:-1,1)
    grani_tmp(white_gran,2,:)=grani(white_gran,3:1:-1,2)
    grani_tmp(white_gran,3,:)=grani(white_gran,3:1:-1,3)
    grani_tmp(orange_gran,3,:)=grani(green_gran,3:1:-1,1)
    grani_tmp(green_gran,:,1)=grani(red_gran,3,:)
    grani_tmp(red_gran,3,:)=grani(blue_gran,3:1:-1,1)
    grani_tmp(blue_gran,:,1)=grani(orange_gran,3,:)
    grani=grani_tmp
enddo
case(green_gran)
do i=1,4-napravlenie
    grani_tmp=grani
    grani_tmp(green_gran,1,:)=grani(green_gran,3:1:-1,1)
    grani_tmp(green_gran,2,:)=grani(green_gran,3:1:-1,2)
    grani_tmp(green_gran,3,:)=grani(green_gran,3:1:-1,3)
    grani_tmp(orange_gran,:,1)=grani(white_gran,:,1)
    grani_tmp(white_gran,:,1)=grani(red_gran,3:1:-1,1)
    grani_tmp(red_gran,:,1)=grani(yellow_gran,:,1)
    grani_tmp(yellow_gran,:,1)=grani(orange_gran,3:1:-1,1)
    grani=grani_tmp
enddo
case(blue_gran)
do i=1,napravlenie
    grani_tmp=grani
    grani_tmp(blue_gran,1,:)=grani(blue_gran,3:1:-1,1)
    grani_tmp(blue_gran,2,:)=grani(blue_gran,3:1:-1,2)
    grani_tmp(blue_gran,3,:)=grani(blue_gran,3:1:-1,3)
    grani_tmp(orange_gran,:,3)=grani(white_gran,:,3)
    grani_tmp(white_gran,:,3)=grani(red_gran,3:1:-1,3)
    grani_tmp(red_gran,:,3)=grani(yellow_gran,:,3)
    grani_tmp(yellow_gran,:,3)=grani(orange_gran,3:1:-1,3)
    grani=grani_tmp
enddo
case(yellow_gran)
do i=1,4-napravlenie
    grani_tmp=grani
    grani_tmp(yellow_gran,1,:)=grani(yellow_gran,3:1:-1,1)
    grani_tmp(yellow_gran,2,:)=grani(yellow_gran,3:1:-1,2)
    grani_tmp(yellow_gran,3,:)=grani(yellow_gran,3:1:-1,3)
    grani_tmp(orange_gran,1,:)=grani(green_gran,3:1:-1,3)
    grani_tmp(green_gran,:,3)=grani(red_gran,1,:)
    grani_tmp(red_gran,1,:)=grani(blue_gran,3:1:-1,3)
    grani_tmp(blue_gran,:,3)=grani(orange_gran,1,:)
    grani=grani_tmp
enddo
case(orange_gran)
do i=1,napravlenie
    grani_tmp=grani
    grani_tmp(orange_gran,1,:)=grani(orange_gran,3:1:-1,1)
    grani_tmp(orange_gran,2,:)=grani(orange_gran,3:1:-1,2)
    grani_tmp(orange_gran,3,:)=grani(orange_gran,3:1:-1,3)
    grani_tmp(white_gran,1,:)=grani(blue_gran,1,:)
    grani_tmp(blue_gran,1,:)=grani(yellow_gran,1,3:1:-1)
    grani_tmp(yellow_gran,1,:)=grani(green_gran,1,:)
    grani_tmp(green_gran,1,:)=grani(white_gran,1,3:1:-1)
    grani=grani_tmp
enddo
case default
end select
end subroutine povorot_on_facelet

!                       \EA\EE\ED\E2\E5\F0\F2\E0\F6\E8\FF \EA\F3\E1\E8\EA\EE\E2 \E2 \EA\EE\EE\F0\E4\E8\ED\E0\F2\FB

subroutine ugol_pack_orientation(ugol,x)
implicit none
integer*1,intent(in)::ugol(8)
integer*2,intent(out)::x
integer*1::i
x=0
do i=8,2,-1
    x=x+ugol(i)*(3**(8-i))
enddo
end subroutine ugol_pack_orientation

subroutine ugol_unpack_orientation(ugol,xx)
implicit none
integer*1,intent(out)::ugol(8)
integer*2,intent(in)::xx
integer*1::i
integer*2::x
ugol=0
x=xx
do i=8,2,-1
    ugol(i)=x-((x/3)*3)
    x=x/3
enddo
ugol(1)=mod(3-mod(sum(ugol(2:8)),3),3)
end subroutine ugol_unpack_orientation

subroutine ugol_pack_perestanovka(ugol,x)
implicit none
integer*1,intent(in)::ugol(8)
integer*4,intent(out)::x
integer*1::i,sum,j,ugol_poradok(7)
integer*4::factorial
x=0
do i=1,7       !!!  \F7\E8\F1\EB\EE
sum=0
    do j=1,8   !!! \F1\F7\E8\F2\E0\E5\EC \EF\EE \E2\EE\E7\F0\E0\F1\F2\E0\ED\E8\FE
    if(ugol(j).gt.i)sum=sum+1
    if(ugol(j).eq.i)then
        ugol_poradok(i)=sum
        exit
    endif
    enddo
enddo
factorial=1
do i=7,1,-1
    factorial=factorial*(8-i)
    x=x+ugol_poradok(i)*factorial
enddo
end subroutine ugol_pack_perestanovka

subroutine ugol_unpack_perestanovka(ugol,xx)
implicit none
integer*1,intent(out)::ugol(8)
integer*4,intent(in)::xx
integer*4::x
integer*1::i,j,ugol_poradok(7),sum
x=xx
ugol=8
do i=1,7
    ugol_poradok(8-i)=x-x/(i+1)*(i+1)
    x=x/(i+1)
enddo
do i=1,7
sum=0
do j=1,8
    if(ugol(j).gt.i)sum=sum+1
    if(sum.eq.ugol_poradok(i)+1)then
    ugol(j)=i
    exit
    endif
enddo
enddo
end subroutine ugol_unpack_perestanovka

subroutine rebro_pack_orientation(rebro,x)
implicit none
integer*1,intent(in)::rebro(12)
integer*2,intent(out)::x
integer*1::i
x=0
do i=12,2,-1
    x=x+rebro(i)*(2**(12-i))
enddo
end subroutine rebro_pack_orientation

subroutine rebro_unpack_orientation(rebro,xx)
implicit none
integer*1,intent(out)::rebro(12)
integer*2,intent(in)::xx
integer*1::i
integer*2::x
rebro=0
x=xx
do i=12,2,-1
    rebro(i)=x-((x/2)*2)
    x=x/2
enddo
rebro(1)=mod(2-mod(sum(rebro(2:12)),2),2)
end subroutine rebro_unpack_orientation

subroutine rebro_pack_perestanovka(rebro,xx)
implicit none
integer*1,intent(in)::rebro(12)
integer*4,intent(out)::xx
integer*1::rebro_cut(8),i
xx=0
rebro_cut(1:4)=rebro(1:4)
rebro_cut(5:8)=rebro(9:12)
do i=1,8
    if(rebro_cut(i).gt.8)rebro_cut(i)=rebro_cut(i)-4
enddo
call ugol_pack_perestanovka(rebro_cut,xx)
end subroutine rebro_pack_perestanovka

subroutine rebro_unpack_perestanovka(rebro,x)
implicit none
integer*4,intent(in)::x
integer*1,intent(out)::rebro(12)
integer*1::rebro_cut(8)
call ugol_unpack_perestanovka(rebro_cut,x)
where(rebro_cut.ge.5)rebro_cut=rebro_cut+4
rebro=(/1,2,3,4,5,6,7,8,9,10,11,12/)
rebro(1:4)=rebro_cut(1:4)
rebro(9:12)=rebro_cut(5:8)
end subroutine rebro_unpack_perestanovka

subroutine rebro_pack_z1(rebro_perestanovka,z_coord)
implicit none
integer*1,intent(in)::rebro_perestanovka(12)
integer*2,intent(out)::z_coord
integer*1::rebro(12),a1,a2,a3,a4,i
integer*2::z
rebro(9:12)=rebro_perestanovka(9:12)
rebro(5:8)=rebro_perestanovka(1:4)
rebro(1:4)=rebro_perestanovka(5:8)
where(rebro.ge.5.and.rebro.le.8)
    rebro=1
else where
    rebro=0
end where
do i=12,1,-1
if(rebro(i).eq.1)then
    a1=i
    rebro(i)=0
    exit
endif
enddo
do i=12,1,-1
if(rebro(i).eq.1)then
    a2=i
    rebro(i)=0
    exit
endif
enddo
do i=12,1,-1
if(rebro(i).eq.1)then
    a3=i
    rebro(i)=0
    exit
endif
enddo
do i=12,1,-1
if(rebro(i).eq.1)then
    a4=i
    rebro(i)=0
    exit
endif
enddo
z_coord=z(a1,a2,a3,a4)
end subroutine rebro_pack_z1

subroutine rebro_unpack_z1(rebro,z1)
implicit none
integer*1,intent(out)::rebro(12)
integer*2,intent(in)::z1
integer*2::zk,i,c
integer*1::a1,a2,a3,a4,rebro_tmp(12)
zk=z1+1
do i=4,12
    if(c(i,4).ge.zk)exit
enddo
a1=i
zk=zk+c(i-1,3)-c(i,4)
do i=3,12
    if(c(i,3).ge.zk)exit
enddo
a2=i
zk=zk+c(i-1,2)-c(i,3)
do i=2,12
    if(c(i,2).ge.zk)exit
enddo
a3=i
zk=zk+c(i-1,1)-c(i,2)
do i=1,12
    if(c(i,1).ge.zk)exit
enddo
a4=i
rebro=0
rebro(a1)=5
rebro(a2)=6
rebro(a3)=7
rebro(a4)=8
rebro_tmp(5:8)=rebro(1:4)
rebro_tmp(1:4)=rebro(5:8)
rebro_tmp(9:12)=rebro(9:12)
rebro=rebro_tmp
a1=0
do i=1,12
    if(rebro(i).eq.0)then
    a1=a1+1
   if(a1.eq.5)a1=a1+4
    rebro(i)=a1
    endif
enddo
end subroutine rebro_unpack_z1

subroutine rebro_unpack_z2(rebro,z2)
implicit none
integer*1,intent(out)::rebro(12)
integer*1,intent(in)::z2
integer*1::rebro_tmp(12)
call rebro_unpack_perestanovka(rebro,int4(z2))
where(rebro.ge.5.and.rebro.le.8)
    rebro=rebro+4
else where(rebro.ge.9)
    rebro=rebro-4
end where
rebro_tmp(1:4)=rebro(1:4)
rebro_tmp(5:8)=rebro(9:12)
rebro_tmp(9:12)=rebro(5:8)
rebro=rebro_tmp
end subroutine rebro_unpack_z2

subroutine rebro_pack_z2(rebro,xx)
implicit none
integer*1,intent(in)::rebro(12)
integer*1,intent(out)::xx
integer*1::rebro_cut(8),i
integer*4::x
xx=0
rebro_cut(1:4)=(/1,2,3,4/)
rebro_cut(5:8)=rebro(5:8)
call ugol_pack_perestanovka(rebro_cut,x)
xx=x
end subroutine rebro_pack_z2

integer*2 function z(a1,a2,a3,a4)
implicit none
integer*1::a1,a2,a3,a4
integer*2::c
z=c(int2(a1),4)-c(int2(a1-1),3)+c(int2(a2),3)-c(int2(a2-1),2)+c(int2(a3),2)-c(int2(a3-1),1)+c(int2(a4),1)-1
end function z

integer*2 function c(n,k)
implicit none
integer*2,intent(in)::n,k
integer*1::i
c=1
do i=n,n-k+1,-1
    c=c*i
enddo
do i=1,k
    c=c/i
enddo
end function c


subroutine cube_solver(grani,hods)
use tables
implicit none
integer*1,intent(in)::grani(6,3,3)
integer*1,intent(out)::hods(max_deep_perebor)
integer*1::facelet(54),facelet_2_fase(54),ugol(8),ugol_p(8),rebro(12),rebro_p(12)
integer*2::x1,y1,z1
integer*4::x2,y2
integer*1::z2
integer*1::hods_1(max_deep_perebor),hods_2(max_deep_perebor),hods_1_start(max_deep_perebor)
integer*1::i,j,dlina1,dlina2

call grani_to_facelet(grani,facelet)
call facelet_to_ugol_table_orientation(facelet,ugol)
call facelet_to_rebro_table_orientation(facelet,rebro)
call facelet_to_rebro_table_perestanovka(facelet,rebro_p)
call ugol_pack_orientation(ugol,x1)
call rebro_pack_orientation(rebro,y1)
call rebro_pack_z1(rebro_p,z1)
hods_1_start=(/0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0/)
call fase_1_next_solution(x1,y1,z1,hods_1_start,hods_1)

call posl_povorot_in_facelet(facelet,facelet_2_fase,hods_1(1:max_deep_perebor))
call facelet_to_ugol_table_perestanovka(facelet_2_fase,ugol_p)
call facelet_to_rebro_table_perestanovka(facelet_2_fase,rebro_p)
call ugol_pack_perestanovka(ugol_p,x2)
call rebro_pack_perestanovka(rebro_p,y2)
call rebro_pack_z2(rebro_p,z2)
call fase_2(x2,y2,z2,hods_2)

do j=1,max_deep_perebor
    hods_2(j)=povorots_2(hods_2(j))
enddo
do i=1,max_deep_perebor
    if(hods_1(i).ne.0)exit
enddo
do j=1,max_deep_perebor
    if(hods_2(j).ne.0)exit
enddo
hods=0
dlina1=max_deep_perebor+1-i
dlina2=max_deep_perebor+1-j
hods(1:dlina1)=hods_1(i:max_deep_perebor)
hods(dlina1+1:dlina1+dlina2)=hods_2(j:max_deep_perebor)
end subroutine cube_solver


subroutine fase_1_next_solution(x_start,y_start,z_start,hods_old,hods_new)
use tables
implicit none
integer*2,intent(in)::x_start,y_start,z_start
integer*1,intent(in)::hods_old(max_deep_perebor)
integer*1,intent(out)::hods_new(max_deep_perebor)
integer*2::hods_x_tmp(0:max_deep_perebor),hods_y_tmp(0:max_deep_perebor),hods_z_tmp(0:max_deep_perebor)
integer*1::deep,np,hods(0:max_deep_perebor)
integer*2::x,y,z

hods(1:max_deep_perebor)=hods_old
hods(0)=0
hods_x_tmp(0)=x_start
hods_y_tmp(0)=y_start
hods_z_tmp(0)=z_start
do deep=max_deep_perebor,1,-1
    if(hods(deep).ne.0)exit
enddo
if(deep.ne.0)then
    hods(deep)=hods(deep)+1
    do np=deep,1,-1
        if(hods(np).eq.19)then
            hods(np-1)=hods(np-1)+1
            hods(np)=0
        else
            exit
        endif
    enddo
    do deep=max_deep_perebor,1,-1
        if(hods(deep).ne.0)exit
    enddo
else
    deep=1
endif
do np=1,max_deep_perebor
    hods_x_tmp(np)=x1_move(hods(np),hods_x_tmp(np-1))
    hods_y_tmp(np)=y1_move(hods(np),hods_y_tmp(np-1))
    hods_z_tmp(np)=z1_move(hods(np),hods_z_tmp(np-1))
enddo

mega: do while(deep.le.max_deep_perebor)
    do np=hods(deep),18
        if(hod_po_predhod(hods(deep-1),np).eq.0)cycle
        x=x1_move(np,hods_x_tmp(deep-1))
        y=y1_move(np,hods_y_tmp(deep-1))
        z=z1_move(np,hods_z_tmp(deep-1))
        if(xy1_deep(x,y).gt.max_deep_perebor-deep.or.xz1_deep(x,z).gt.max_deep_perebor-deep.or.yz1_deep(y,z).gt.max_deep_perebor-deep)then
            cycle
        else
            hods(deep)=np
            if(deep+1.lt.max_deep_perebor)hods(deep+1)=0
            hods_x_tmp(deep)=x
            hods_y_tmp(deep)=y
            hods_z_tmp(deep)=z
            deep=deep+1
            cycle mega
        endif
    enddo
    deep=deep-1
    hods(deep)=hods(deep)+1
enddo mega
hods_new=hods(1:max_deep_perebor)
end subroutine fase_1_next_solution

subroutine fase_2(x_start,y_start,z_start,hods_new)
use tables
implicit none
integer*4,intent(in)::x_start,y_start
integer*1,intent(in)::z_start
integer*1,intent(out)::hods_new(max_deep_perebor)
integer*4::hods_x_tmp(0:max_deep_perebor),hods_y_tmp(0:max_deep_perebor)
integer*1::hods_z_tmp(0:max_deep_perebor)
integer*1::deep,np,hods(0:max_deep_perebor),ind_pov
integer*4::x,y
integer*1::z

hods(:)=0
hods_x_tmp(0)=x_start
hods_y_tmp(0)=y_start
hods_z_tmp(0)=z_start
deep=1

mega: do while(deep.le.max_deep_perebor)
    do ind_pov=hods(deep),10
        np=povorots_2(ind_pov)
        if(hod_po_predhod(povorots_2(hods(deep-1)),np).eq.0)cycle
        x=x2_move(ind_pov,hods_x_tmp(deep-1))
        y=y2_move(ind_pov,hods_y_tmp(deep-1))
        z=z2_move(ind_pov,hods_z_tmp(deep-1))
        !if(x2_deep(x).gt.max_deep_perebor-deep.or.y2_deep(y).gt.max_deep_perebor-deep.or.z2_deep(z).gt.max_deep_perebor-deep)then
        if(xz2_deep(x,z).gt.max_deep_perebor-deep.or.yz2_deep(y,z).gt.max_deep_perebor-deep)then
            cycle
        else
            hods(deep)=ind_pov
            if(deep+1.lt.max_deep_perebor)hods(deep+1)=0
            hods_x_tmp(deep)=x
            hods_y_tmp(deep)=y
            hods_z_tmp(deep)=z
            deep=deep+1
            cycle mega
        endif
    enddo
    deep=deep-1
    hods(deep)=hods(deep)+1
enddo mega
hods_new=hods(1:max_deep_perebor)
end subroutine fase_2