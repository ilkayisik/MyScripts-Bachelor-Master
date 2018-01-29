
// Batch script for BrainVoyagerQX
// VTC preprocessing.
// @author: Ayse Ilkay Isik
// for Afford Project 
// April,2015


var bvqx = BrainVoyagerQX;
bvqx.PrintToLog("---/n---");

var NamePrefix = "P02_Run";
var AnatomicalData = "D:/TEST/P02/Anat/P02_Anat_TAL.vmr";  
var docVMR = bvqx.OpenDocument(AnatomicalData);
var NumberOfRuns = 5;
//----------------------------------------------------------------
//Enter VTC paths

var VTC_List = ["D:\TEST\P02\Func_Run1\P02_Run1.vtc",
"D:\TEST\P02\Func_Run2\P02_Run2.vtc",
"D:\TEST\P02\Func_Run3\P02_Run3.vtc",
"D:\TEST\P02\Func_Run4\P02_Run4.vtc",
"D:\TEST\P02\Func_Run5\P02_Run5.vtc"];

for(count = 1; count <= NumberOfRuns; count++){
	
	var ResultingVTCName =  NamePrefix + String(count) + "_PP";
	var VTC_Path = VTC_List[count-1];
	
    docVMR = bvqx.LinkVTC(VTC_Path);
	docVMR = SpatialGaussianSmoothing(8, "mm");  //Description: Spatial low-pass filter for VTC file; removes spatial high-frequency elements in VTC file, like sharp edges.
	docVMR.SaveAs(ResultingVTCName);
	docVMR.Close();
};

bvqx.PrintToLog("***/nThe_END/n***.");


 