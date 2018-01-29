clear all; close all; clc;
B=400;
PF = @PAL_Logistic;
[StimLevels NumPos OutofNum] = readFiles ('C:\\Users\\Research\\Desktop\\Psyc 405\\4ITWSC\\data_2AFCMTS.txt');
%[StimLevels NumPos] = readFiles ('C:\Users\Research\Desktop\Psyc
%405\2AFC\Data 2AFC.txt');
paramValues = [0 2 0.5 0.01];
paramFree = [1 0 0 0];
[paramValues LL exitFlag] = PAL_PFML_Fit (StimLevels, NumPos, OutofNum, paramValues, paramFree, PF);
StimLevelFine = linspace(0, max(StimLevels), 500);
Fit = PF(paramValues, StimLevelFine);
expData = NumPos ./ OutofNum;
plot (StimLevelFine, Fit, 'k-', StimLevels, expData, 'ko');
ylim ([0 1]);
xlim ([min(StimLevelFine) max(StimLevelFine)]);
set (gca, 'Xtick', (round(StimLevels*100)/100));
%disp ('Bootstrapping...');
%[SD paramsSim LLSim converged] = PAL_PFML_BootstrapParametric (StimLevels, OutofNum, paramValues, paramFree, B, PF);
[Dev pDev DevSim converged] = PAL_PFML_GoodnessOfFit (StimLevels, NumPos, OutofNum, paramValues, paramFree, B, PF);