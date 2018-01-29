% Apply topup with b02b0.cfg
% @author: Omer Faruk Gulban
% modified by Ayse Ilkay Isik for AFFORD project

dp = ''; 

epi_names{1}=fullfile(dp,'P_Run1_PP.fmr');
epi_names{2}=fullfile(dp,'P_Run2_PP.fmr');
epi_names{3}=fullfile(dp,'P_Run3_PP.fmr');
epi_names{4}=fullfile(dp,'P_Run4_PP.fmr');
epi_names{5}=fullfile(dp,'P_Run5_PP.fmr');


%% Load fmr experiment time series, convert to nii
nr_epis=length(epi_names);
for i=1:nr_epis;
    epi{i}=xff(epi_names{i});
    epi{i}.Write4DNifti([epi_names{i}(1:end-3),'nii']);
    epi{i}.ClearObject;
end;

%% Apply topup
for i=1:nr_epis;
    unix(['fsl5.0-applytopup -i ',[epi_names{i}(1:end-3),'nii.gz'],' -a acqparams_unwarp.txt --topup=topup_results --inindex=1 --method=jac --interp=spline --out=',[epi_names{i}(1:end-4),'_corrected.nii.gz']]);
    unix(['gunzip ',[epi_names{i}(1:end-4),'_corrected.nii.gz']]);
    unix(['rm -rf ',[epi_names{i}(1:end-4),'_corrected.nii.gz']]);
    tempnii=xff([epi_names{i}(1:end-4),'_corrected.nii']);
    % Convert back to .fmr
    tempfmr=tempnii.Dyn3DToFMR;
    fmr_prop=xff(epi_names{i});
    % Position information
    fmr_prop.Slice.STCData=tempfmr.Slice.STCData;
    % Save
    fmr_prop.SaveAs([epi_names{i}(1:end-4),'_TU.fmr']);
    unix(['rm -rf ',[epi_names{i}(1:end-4),'_corrected.nii']]);
    unix(['rm -rf ',[epi_names{i}(1:end-4),'.nii']]);
    disp(['fmr ' num2str(i) ' of ' num2str(nr_epis) ' computed']);
end;



