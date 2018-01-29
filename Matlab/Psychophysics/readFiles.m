function [angles, counts, total] = readFiles (fileName)
    angles = []; counts = []; total = [];
    fid = fopen (fileName, 'r');
    if (fid == -1)
        disp ('ERROR: Cannot read file');
        return;
    end
    data = fscanf (fid, '%d %f %d');
    
    fclose(fid);
    
    for i=0:(floor(length(data)/3)-1)
        jj = 0;
        if (length(angles)>0)
            for j=1:length(angles)
                if(angles (j) == abs(data(3*i+2))), jj = j; end
            end
        end
        if (jj == 0)
            jj = length(angles)+1;
            angles(jj) = abs(data(3*i+2));
            counts(jj) = 0;
            total (jj) = 0;
        end;
        counts(jj) = counts(jj) + data(3*(i+1));
        total (jj) = total (jj) + 1;
    end
    
    [angles sortIndex] = sort(angles);
    counts = counts(sortIndex);
    total  = total (sortIndex);
    
end