fileName = 'C:\\Users\\Research\\Desktop\\Psyc 405\\4ITWSC\\data_S2AFC.txt';
fid = fopen (fileName, 'r');
data = fscanf (fid, '%d %f %d');
fclose(fid);

data = abs(data);

staircaseNum = data(1:3:length(data));
level =        data(2:3:length(data));
response =     data(3:3:length(data));

figure, hold on;
plot (level(staircaseNum == 0), 'r');
plot (level(staircaseNum == 1), 'g');
plot (level(staircaseNum == 2), 'b');
plot (level(staircaseNum == 3), 'k');

plot((level(staircaseNum == 3) + level(staircaseNum == 2) + level(staircaseNum == 1) + level(staircaseNum == 0))/4, 'k:');