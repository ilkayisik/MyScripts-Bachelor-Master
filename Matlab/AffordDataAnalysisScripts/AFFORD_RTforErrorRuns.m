clear all
 
VpName=input('Vp.txt:', 's');    %insert "subjects'-name.txt"
data=dlmread(VpName,'\t');   %this feeds the data into the script




Response = zeros (80,1);
v =1;

for t = 1:length(data)
    if data (t,1) == 1;  
        Response(v,1) = 1;
        v = v +1;
    elseif data (t,1) == 2;
        Response(v,1) = 2;
        v = v +1;
    elseif data(t,1) ~= 1 || data(t,1) ~= 2;
        v = v;
    end
    
end
 
data(data(:,1)==55,:)=[];
data(data(:,1)==20,:)=[];


RT = ones (80,1);
a=1;


for i = 1: length(data)
    if data (i,1) == 1;
        RT(a,1) = data (i,2) - data (i-1,2);
        a = a+1;
        
    elseif data (i,1) == 2;
        RT(a,1) = data (i,2) - data (i-1,2);
        a = a+1;
    end
end

  


   