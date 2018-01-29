clear all;


VpName=input('Vp.txt:', 's');    %insert "subjects'-name.txt"
daten=dlmread(VpName,'\t');

i = 1;
j = 1;
k = 1;
a = 1;

for i=1:(length(daten)-1)                                           %check if somewhere is a response before the stimulus
    if daten (i,1) == 10 && daten (i+1,1) < 20
        CheckForErrors (j,1) = i+1;
        j=j+1;
    end      
end


for i=1:(length(daten)-1)                                           %check if somewhere are multiple responses in a row
    if daten (i,1) < 10 && daten (i+1,1) < 10
        CheckForErrors (k,2) = i+1;
        k=k+1;
    end      
end


for i=1:(length(daten)-1)                                           %check if there is a missing response
    if daten (i,1) > 19 && daten (i+1,1) == 10
        CheckForErrors (a,3) = i;
        a=a+1;
    end      
end

dlmwrite('PleaseCheckForErrors.txt',CheckForErrors,'\t');
