

clear all
 
VpName=input('Vp.txt:', 's');    %insert "subjects'-name.txt"
daten=dlmread(VpName,'\t');   %this feeds the data into the script

a = 1;
b = 1;
c = 1;
d = 1;
TrialNr = 0;
LeftHand_Hits = 0;
LeftHand_Errors = 0;
RightHand_Hits = 0;
RightHand_Errors = 0;
FalseAlarms = 0;
TrialErrors = 0;
 
% check for left/right button presses
for i=1:length(daten)
        if daten (i,1)== 100
            TrialNr = TrialNr + 1;
            for a = 1:10
                if daten (i+a,2) == 1
                    LeftHand_Hits = LeftHand_Hits + 1;
                    a = a + 1;
                else LeftHand_Errors = LeftHand_Errors + 1;
                    a = a +1;
                    TrialErrors (c,1) = TrialNr;
                    c = c + 1;
                end
            end
            a = 1;
        elseif daten (i,1) == 200
            TrialNr = TrialNr + 1;
            for b = 1:10
                if daten (i+b,2) == 2
                    RightHand_Hits = RightHand_Hits + 1;
                    b = b + 1;
                else RightHand_Errors = RightHand_Errors + 1;
                    b = b +1;
                    TrialErrors (c,1) = TrialNr;
                    c = c + 1;
                end
            end
            b = 1;
        elseif daten (i,1) >= 300 && daten (i,1) <= 400  %While counting trials exclude code 500 and only count (100,200,300,400)
            TrialNr = TrialNr + 1;
            if daten (i,2) ~= 0
                FalseAlarms (d,1) = TrialNr;
                d = d + 1;
            end
        end
end

% LeftHand_Hits; RightHand_Hits; LeftHand_Errors; RightHand_Errors, % TrialErrors; FalseAlarms


NewSubject (1,1) = LeftHand_Hits;
NewSubject (1,2) = RightHand_Hits;
NewSubject (1,3) = LeftHand_Errors;
NewSubject (1,4) = RightHand_Errors;
NewSubject (1:length(TrialErrors),5) = TrialErrors (:,1);
NewSubject (1:length(FalseAlarms),6) = FalseAlarms (:,1);

 
dlmwrite ('NewSubject.txt', NewSubject, '\t');         





