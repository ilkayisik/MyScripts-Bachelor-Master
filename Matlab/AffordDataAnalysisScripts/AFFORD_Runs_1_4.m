clear all
 
VpName=input('Vp.txt:', 's');    %insert "subjects'-name.txt"
daten=dlmread(VpName,'\t');   %this feeds the data into the script

daten(daten(:,1)==9,:)=[];   %alle "9"-Zeilen löschen
daten(:,3)= daten(:,3)./10;     %transform in ms


Trials = zeros (10,8);
Trialerrors = zeros (1,8);

a1 = 1;
a2 = 1;
a3 = 1;
a4 = 1;
a5 = 1;
a6 = 1;
a7 = 1;
a8 = 1;

% code 1 = left button
% code 2 = right button
% code 9 = fixation cross
% code 10... = CLOLH
% code 20... = CLORH
% code 30... = CROLH
% code 40... = CRORH
% code 50... = SLOLH
% code 60... = SLORH
% code 70... = SROLH
% code 80... = SRORH
%Trials matrix (reaction times):            CLOLH CROLH CRORH CLORH SLOLH SROLH SRORH SLORH
%Trialerrors matrix (number of errors):     CLOLH CROLH CRORH CLORH SLOLH SROLH SRORH SLORH

 
for i = 1:length(daten)
    if daten (i,1) >= 100 && daten (i,1) < 200 %CLOLH
        if daten (i,2) == 1
            Trials (a1,1) = daten (i,3);
            a1 = a1 + 1;
        elseif daten (i,2) ~= 1
            Trialerrors (1,1) = Trialerrors (1,1) + 1;
        end
    elseif daten (i,1) >= 200 && daten (i,1) < 300 %CLORH
        if daten (i,2) == 2
            Trials (a2,4) = daten (i,3);
            a2 = a2 + 1;
        elseif daten (i,2) ~= 2
            Trialerrors (1,4) = Trialerrors (1,4) + 1; 
        end
    elseif daten (i,1) >= 300 && daten (i,1) < 400 %CROLH
        if daten (i,2) == 1
            Trials (a3,2) = daten (i,3);
            a3 = a3 + 1;
        elseif daten (i,2) ~= 1
            Trialerrors (1,2) = Trialerrors (1,2) + 1; 
        end
    elseif daten (i,1) >= 400 && daten (i,1) < 500 %CRORH
        if daten (i,2) == 2
            Trials (a4,3) = daten (i,3);
            a4 = a4 + 1;
        elseif daten (i,2) ~= 2
            Trialerrors (1,3) = Trialerrors (1,3) + 1;
        end
    elseif daten (i,1) >= 500 && daten (i,1) < 600 %SLOLH
        if daten (i,2) == 1
            Trials (a5,5) = daten (i,3);
            a5 = a5 + 1;
        elseif daten (i,2) ~= 1
            Trialerrors (1,5) = Trialerrors (1,5) + 1;
        end
    elseif daten (i,1) >= 600 && daten (i,1) < 700  %SLORH
        if daten (i,2) == 2
            Trials (a6,8) = daten (i,3);
            a6 = a6 + 1;
        elseif daten (i,2) ~= 2
            Trialerrors (1,8) = Trialerrors (1,8) + 1;
        end
    elseif daten (i,1) >= 700 && daten (i,1) < 800 %SROLH
        if daten (i,2) == 1
            Trials (a7,6) = daten (i,3);
            a7 = a7 + 1;
        elseif daten (i,2) ~= 1
            Trialerrors (1,6) = Trialerrors (1,6) + 1;
        end
    elseif daten (i,1) >= 800  %SRORH
        if daten (i,2) == 2
            Trials (a8,7) = daten (i,3);
            a8 = a8 + 1;
        elseif daten (i,2) ~= 2
            Trialerrors (1,7) = Trialerrors (1,7) + 1;
        end
    end
    i = i +1;
end
    

dlmwrite ('NewSubject_RT.txt', Trials, '\t');
dlmwrite ('NewSubject_Errors.txt', Trialerrors, '\t');         





