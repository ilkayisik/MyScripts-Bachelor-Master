// Batch script for BrainVoyagerQX
// VTC creation. Native to ACPC to TAL
// @author: Omer Faruk Gulban 
// Modified by Ayse Ilkay Isik for Afford Project


var bvqx = BrainVoyagerQX;
bvqx.PrintToLog("---/n---");

//----------------------------------------------------------------
// Enter the following values:
var MainDataPath = "D:/TEST/P02/";
var RunPrefix = "Func_Run";
var NumberOfRuns = 5;
var AnatomicalData = "D:/TEST/P02/Anat/P02_Anat_TAL.vmr";  




var IA_TRFList = ["D:/TEST/P02/Func_Run1/P02_Run1_PP_TU-TO-P02_Anat_IA.trf",
      "D:/TEST/P02/Func_Run2/P02_Run2_PP_TU-TO-P02_Anat_IA.trf",
	"D:/TEST/P02/Func_Run3/P02_Run3_PP_TU-TO-P02_Anat_IA.trf",
	"D:/TEST/P02/Func_Run4/P02_Run4_PP_TU-TO-P02_Anat_IA.trf",
	"D:/TEST/P02/Func_Run5/P02_Run5_PP_TU-TO-P02_Anat_IA.trf",];

var FA_TRFList = ["D:/TEST/P02/Func_Run1/P02_Run1_PP_TU-TO-P02_Anat_FA_Manual.trf",
"D:/TEST/P02/Func_Run2/P02_Run2_PP_TU-TO-P02_Anat_FA_Manual.trf",
"D:/TEST/P02/Func_Run3/P02_Run3_PP_TU-TO-P02_Anat_FA_Manual.trf",
"D:/TEST/P02/Func_Run4/P02_Run4_PP_TU-TO-P02_Anat_FA_Manual.trf",
"D:/TEST/P02/Func_Run5/P02_Run5_PP_TU-TO-P02_Anat_FA_Manual.trf"];

var ACPC_TRFList = ["D:/TEST/P02/Anat/P02_Anat_ACPC.trf"];
var TAL_List = ["D:/TEST/P02/Anat/P02_Anat_ACPC.tal"];

var InFmrList = ["D:/TEST/P02/Func_Run1/P02_Run1_PP_TU.fmr",
"D:/TEST/P02/Func_Run2/P02_Run2_PP_TU.fmr",
"D:/TEST/P02/Func_Run3/P02_Run3_PP_TU.fmr",
"D:/TEST/P02/Func_Run4/P02_Run4_PP_TU.fmr",
"D:/TEST/P02/Func_Run5/P02_Run5_PP_TU.fmr"];

//Output Talairach vtc file names
var OutVTCNameList = ["P02_Run1.vtc",
    "P02_Run2.vtc",
    "P02_Run3.vtc",
    "P02_Run4.vtc",
    "P02_Run5.vtc"];

//----------------------------------------------------------------

// Open anatomical data
var docVMR = bvqx.OpenDocument(AnatomicalData);

for(count = 1; count <= NumberOfRuns; count++){
	
    var IA_Path = IA_TRFList[count-1];
    var FA_Path = FA_TRFList[count-1];
    var InFmrPath = InFmrList[count-1];    
    var OutVTCName = OutVTCNameList[count-1];  
    var ACPC_TRFPath = ACPC_TRFList[0]
	var TAL_Path =TAL_List[0]
	
    // Create VTC in TAL Space
    docVMR.CreateVTCInTALSpace(InFmrPath, 
                                IA_Path,  // IA.trf
                                FA_Path,  // FA.trf
                                ACPC_TRFPath,  // *_ACPC.trf
                                TAL_Path,  // *.tal
                                OutVTCName,  // output name
                                2,   // datatype integer 2-byte format:1 or float format:2
                                3,   // target resolution 1x1x1:1 or 2x2x2:2 or 3x3x3:3
                                1,   // nearest neighbour:0 or trilinear:1 or sinc:2
                                100  // threshold(Default value:100)
                                );
                       
    bvqx.PrintToLog(OutVTCName + " finished.");
};

