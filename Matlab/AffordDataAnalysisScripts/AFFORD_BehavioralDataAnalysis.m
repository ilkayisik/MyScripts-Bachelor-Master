clear all

%% Read from text file
textList = {'P32_Run1_RT.txt',...
            'P32_Run2_RT.txt',...
            'P32_Run3_RT.txt',...
            'P32_Run4_RT.txt' ...
            };

data = {}
for i=1:4
    fileID = fopen(textList{1});
    data{i} = textscan(fileID, '%f %f %f %f %f %f %f %f', ...
                       'Delimiter', '	', ...
                       'EmptyValue' , NaN);
    fclose(fileID);
end

% Cell to matrix conversion
matrix = {};
matrix{1} = cell2mat(data{1});
matrix{2} = cell2mat(data{2});
matrix{3} = cell2mat(data{3});
matrix{4} = cell2mat(data{4});
matrix = cell2mat(matrix);
matrix = reshape(matrix, 10, 8, 4);
matrix(matrix==0) = NaN;

% Desired descriptive statistics
output = nan(7, 8);
output(1,:) = min(matrix(:,:,1),[],1)
output(2,:) = max(matrix(:,:,1),[],1)
output(3,:) = nanmean(matrix(:,:,1),1)
output(4,:) = nanstd(matrix(:,:,1),1)
output(5,:) = nanmedian(matrix(:,:,1),1)
run1 = matrix(:,:,1);
output(6,1) = nanmean(run1(:))+3*nanstd(run1(:))
output(7,1) = nanmean(run1(:))-3*nanstd(run1(:))

% % Save as a text file
% fid = fopen('Desc_Stats.txt', 'w' );
% for row = 1:size(output,1)
%     fprintf(fid, '%f, %f, %f, %f, %f, %f, %f, %f, \r\n', output{row,:});
% end
% fclose(fid);

csvwrite('Desc_Stats.csv', output)

% advanced solution (WIP)
% matrix2 = reshape(matrix, 10, 2, 2, 2, 4);
% matrix2 = permute(matrix2, [1,2,4,3,5]);

% Collapse desired dimensions (TODO)
[run1(:,1) run1(:,2);run1(:,3) run1(:,4)]