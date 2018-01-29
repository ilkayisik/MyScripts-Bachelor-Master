// Batch script for BVQX
// Renaming Dicom Files
// @author: Ayse Ilkay Isik for AFFORD Project


var bvqx = BrainVoyagerQX;
bvqx.PrintToLog("---/n---");

var NumberOfFolders = 8;

// Enter fmr file paths
var InFmrList = ["D:/TEST/P03",
"D:/TEST/P04",
"D:/TEST/P05",
"D:/TEST/P06",
"D:/TEST/P07",
"D:/TEST/P08",
"D:/TEST/P09",
"D:/TEST/P10"];



for(count = 1; count <= NumberOfFolders; count++){
	
var InFmrPath = InFmrList[count-1];
bvqx.RenameDicomFilesInDirectory(InFmrPath)

};

bvqx.PrintToLog("***\nThe_END\n***.");