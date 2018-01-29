
% Topup with b02b0.cfg
% @author: Omer Faruk Gulban
% modified by Ayse Ilkay Isik for AFFORD project.


dp = '/media/sf_TEST/P/Topup'; 


% Load AP and PA phase '.fmr'



phaseEncFilename{1} = fullfile(dp,'P_PA_Run1.fmr');
phaseEncFilename{2} = fullfile(dp,'P_AP_Run1.fmr');
phaseEncFilename{3} = fullfile(dp,'P_PA_Run2.fmr');
phaseEncFilename{4} = fullfile(dp,'P_AP_Run2.fmr');
phaseEncFilename{5} = fullfile(dp,'P_PA_Run3.fmr');
phaseEncFilename{6} = fullfile(dp,'P_AP_Run3.fmr');
phaseEncFilename{7} = fullfile(dp,'P_PA_Run4.fmr');
phaseEncFilename{8} = fullfile(dp,'P_AP_Run4.fmr');
phaseEncFilename{9} = fullfile(dp,'P_PA_Run5.fmr');
phaseEncFilename{10} = fullfile(dp,'P_AP_Run5.fmr');
%% Convert to nii

for i=1:2:9
    
        phaseEnc{i} = xff(phaseEncFilename{i}); 
        phaseEnc{i}.Write4DNifti([phaseEncFilename{i}(1:end-3),'nii']);
        phaseEnc{i}.ClearObject;

        phaseEnc{i+1} = xff(phaseEncFilename{i+1}); 
        phaseEnc{i+1}.Write4DNifti([phaseEncFilename{i+1}(1:end-3),'nii']);
        phaseEnc{i+1}.ClearObject;

end
%% Merge
unix(['fsl5.0-fslmerge -t up_down_phase ',...
     [phaseEncFilename{i}(1:end-3),'nii '],...
     [phaseEncFilename{i+1}(1:end-3),'nii']]);

%% Topup
unix('fsl5.0-topup --imain=up_down_phase --datain=acqparams.txt --config=b0.cnf --out=topup_results');
disp('top up computed')