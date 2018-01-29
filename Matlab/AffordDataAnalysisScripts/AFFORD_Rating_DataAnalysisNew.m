clear all;



VpName=input('Vp.txt:', 's');    %insert "subjects'-name.txt"
daten=dlmread(VpName,'\t');

daten(daten(:,1)==10,:)=[];

test = daten;


a = 1;
b = 1;
c = 1;
d = 1;
e = 1;
nothing = 1;




for e=1:10:200                                           
    
            if daten (e,1) > 19 && daten (e,1) < 25
                ControlLeft(a,1) = daten (e,1)-20;                        % name the stimulus
                ControlLeft(a,2) = daten (e+1,1);                         % craving
                ControlLeft(a,3) = daten (e+3,1);                         % valence
                ControlLeft(a,4) = daten (e+5,1);                         % arousal
                ControlLeft(a,5) = daten (e+7,1);                        % familiarity
                ControlLeft(a,6) = daten (e+9,1);                        % motor association
                a=a +1;
                
            elseif daten (e,1) > 29 && daten (e,1) < 35
                ControlRight(b,1) = daten (e,1)-30;                        % name the stimulus
                ControlRight(b,2) = daten (e+1,1);                         % craving
                ControlRight(b,3) = daten (e+3,1);                         % valence
                ControlRight(b,4) = daten (e+5,1);                         % arousal
                ControlRight(b,5) = daten (e+7,1);                        % familiarity
                ControlRight(b,6) = daten (e+9,1);                        % motor association
                b=b+1;
                
            elseif daten (e,1) > 39 && daten (e,1) < 45
                SmokeLeft(c,1) = daten (e,1)-40;                        % name the stimulus
                SmokeLeft(c,2) = daten (e+1,1);                         % craving
                SmokeLeft(c,3) = daten (e+3,1);                         % valence
                SmokeLeft(c,4) = daten (e+5,1);                         % arousal
                SmokeLeft(c,5) = daten (e+7,1);                        % familiarity
                SmokeLeft(c,6) = daten (e+9,1);                        % motor association
                c=c+1;
               
    
                
            elseif daten (e,1) > 49 && daten (e,1) <= 55
                SmokeRight(d,1) = daten (e,1)-50;                        % name the stimulus
                SmokeRight(d,2) = daten (e+1,1);                         % craving
                SmokeRight(d,3) = daten (e+3,1);                         % valence
                SmokeRight(d,4) = daten (e+5,1);                         % arousal
                SmokeRight(d,5) = daten (e+7,1);                        % familiarity
                SmokeRight(d,6) = daten (e+9,1);                        % motor association
                d=d+1;
                
            end
            
        
end
        
    
dlmwrite('ControlLeft.txt',ControlLeft,'\t');
dlmwrite('ControlRight.txt',ControlRight,'\t');
dlmwrite('SmokeLeft.txt',SmokeLeft,'\t');
dlmwrite('SmokeRight.txt',SmokeRight,'\t');


