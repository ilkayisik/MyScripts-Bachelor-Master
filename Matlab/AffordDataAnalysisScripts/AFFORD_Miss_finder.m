clear all

%% Read from text file
logName = 'C:\Users\Ilkay Isik\Desktop\Behavioral_DATA\Behavioral data_Analysis\Smokers\P31\P31_Run2.txt';
fileID = fopen(logName);
log = textscan(fileID, '%f %f %f', ...
               'Delimiter', ' ', ...
               'EmptyValue' , NaN);
fclose(fileID);

% To access: data{1}(1:10)

%% Read BV .prt file line by line
prtName = 'D:\TEST\fMRI_Analysis_FolderOrganization\PROTOCOLs\AFF_Run2.prt';
fileID = fopen(prtName);
prt = textscan(fileID, '%s', ...
               'Delimiter', '\n', ...  % newline as delimiter
               'EmptyValue' , NaN ...
               );
fclose(fileID); 
prt = [prt{:}];  % for easier access to the elements later

%% Find misses in behavioral data
% (Arbitrary) Convert cell structure to matrix format
data = [log{1} log{2} log{3}];

% Number code categories in a matlab struct
categ = struct();
categ.CLOLH = [100 101 102 103 104];
categ.CLORH = [200 201 202 203 204];
categ.CROLH = [300 301 302 303 304];
categ.CRORH = [400 401 402 403 404];
categ.SLOLH = [500 501 502 503 504];
categ.SLORH = [600 601 602 603 604];
categ.SROLH = [700 701 702 703 704];
categ.SRORH = [800 801 802 803 804];
categNames = fieldnames(categ);

% % Category codes after collapsing of the hands
% clear categ
% categ = struct();
% categ.CC = [100 101 102 103 104 400 401 402 403 404];
% categ.CI = [200 201 202 203 204 300 301 302 303 304];
% categ.SC = [500 501 502 503 504 800 801 802 803 804];
% categ.SI = [600 601 602 603 604 700 701 702 703 704];
% categNames = fieldnames(categ);

% Find indices of NaNs (misses) in the second column of data
[row, col] = find( isnan(data(:,2,:)) );
% Find which category the misses belong to
categMiss = nan(size(row,1), 2);
categMiss(:,1) = floor(data(row,1,1)/100);  % Remember the third digit place

%% Count categIndex of the miss in its own category
% % Solution 1
% for i=1:size(categMiss,1)
%     temp = data(1:row(i),1,1);  % consider the relevant chunk in data
%     categMiss(i,2) = sum(temp>=categMiss(i,1)*100 & ...
%                          temp<categMiss(i,1)*100+100 ...
%                          );
% end
% solution1 = categMiss;
% categMiss(:,2) = NaN;  % Reset the findings of solution 1

% Solution 2
for i=1:size(categMiss,1)
    temp = data(1:row(i),1,1);  % consider the relevant chunk in data
    for j=1:size(categNames,1)
        jCategName = categNames{j};  % cell to string conversion
        if ismember(categMiss(i,1), floor(categ.(jCategName)/100) )
            categMiss(i,2) = sum( ismember(temp, categ.(jCategName)) );
        end     
    end
end
solution2 = categMiss;

%% Protocol file manipulations

% Dummy category related
dummyIndex = strfind(prt, 'Dummy');
dummyIndex = find(not(cellfun('isempty', dummyIndex)));
nrDummyTrials = str2double(prt(dummyIndex+1));
dummyEndIndex = dummyIndex+nrDummyTrials+2;
dummy = prt(dummyIndex:dummyEndIndex);

totalEraseCount = 0;  % Will be useful later
dummyUpdate = dummy;

for i=1:size(categNames,1)  
    jCategName = categNames{i};
    % Find row of the category in .prt
    categIndex = strfind(prt, jCategName);
    categIndex = find(not(cellfun('isempty', categIndex)));
    % Find number of trials to determine chunk
    nrTrials = str2double(prt(categIndex+1));
    % Note the end index of the category for clarity
    categEndIndex = categIndex+nrTrials+2;
    % To count how many rows are deleted per condition
    eraseCount = 0;
    for j=1:size(categMiss,1)
        % Find the relevant chunk to operate on
        if ismember(categMiss(j,1), floor(categ.(jCategName)/100) )
            disp('MISS DETECTED!')
            temp = prt(categIndex:categEndIndex);
            % Insert the miss to the DUMMY in the correct row
            dummyPrevious = dummyUpdate;
            dummyUpdate = cell(size(dummyUpdate,1)+1, size(dummyUpdate,2));
            dummyUpdate(1:end-1,1) = dummyPrevious;
            dummyUpdate(end-1) = temp(2+categMiss(j,2));
            dummyUpdate(end) = dummyPrevious(end);
            % Erase the miss from the condition list
            temp(2+categMiss(j,2)) = cellstr(' ');           
            % Plug the chunk back in to its original place
            prt(categIndex:categEndIndex) = temp;
            eraseCount = eraseCount+1;
            totalEraseCount = totalEraseCount+1;
        end
    end
    % Write correct number of trials
    nrTrials = nrTrials - eraseCount;
    prt(categIndex+1) = cellstr(num2str(nrTrials)); 
end

% Insert updated dummy in the place of the old one
prtUpdate = cell(size(prt,1)+totalEraseCount, size(prt,2));
prtUpdate(1:dummyIndex-1) = prt(1:dummyIndex-1);
dummyUpdateEndIndex = dummyIndex+size(dummyUpdate,1)-1; 
prtUpdate(dummyIndex:dummyUpdateEndIndex) = dummyUpdate;
prtUpdate(dummyUpdateEndIndex:end) = prt(dummyEndIndex:end);
% Write correct number of dummy trials
prtUpdate(dummyIndex+1) = cellstr(num2str(nrDummyTrials+totalEraseCount));
disp('DONE.')

%% (Optional) Remove empty string cells
% % find empty cells
% emptyCells = cellfun(@isempty, prtUpdate);
% % remove empty cells
% prtUpdate(emptyCells) = [];

%% Write text file
% Create new name automatically determined by the input files
[logPath logName ~] = fileparts(logName);
[prtPath prtName ~] = fileparts(prtName);
logName = logName(1:8); %write log name in format P0x_RunX
newPrtName = fullfile(prtPath, [prtName,'-',logName,'_Corrected.prt']);
% Create a text file
fid = fopen(newPrtName, 'w' );
for row = 1:size(prtUpdate,1)
    fprintf(fid, '%s \r\n', prtUpdate{row,:});
end
fclose(fid);
disp(['New protocol created at: ' newPrtName]);
