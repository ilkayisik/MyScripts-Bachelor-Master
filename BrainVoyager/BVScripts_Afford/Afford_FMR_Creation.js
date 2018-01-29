//Batch Script for AFFORD Project.
//FMR Creation for Functional Runs
//@author Omer Faruk Gulban
// modified by Ayse Ilkay Isik



var bvqx = BrainVoyagerQX;
bvqx.PrintToLog("---/n---");

//----------------------------------------------------------------
// Enter the following values:

//var MainDataPath = "D:/TEST/P29/Topup/";
//var RunPrefix = "Func_Run_";


// Enter the first dicom file paths per run.
//Change for each participant
var InDicomList =["D:/TEST/P29/0003_bic_epi_v1_nrFiles244_Loc/P29 -0003-0001-00001.dcm",
"D:/TEST/P29/0007_bic_epi_v1_nrFiles244_Run2/P29 -0007-0001-00001.dcm",
"D:/TEST/P29/0011_bic_epi_v1_nrFiles244_Run3/P29 -0011-0001-00001.dcm",
"D:/TEST/P29/0015_bic_epi_v1_nrFiles244_Run4/P29 -0015-0001-00001.dcm",
"D:/TEST/P29/0019_bic_epi_v1_nrFiles244_Run1/P29 -0019-0001-00001.dcm"
];

// Enter the run names.


var RunNameList =["P29_Run5",
                  "P29_Run2",
                  "P29_Run3",
                  "P29_Run4",
                  "P29_Run1"];

var NrVolPerRunList = [244, 244, 244, 244, 244];

var NrOfRuns = InDicomList.length;

for (count = 1; count <=NrOfRuns; count++){


	var RunPath =  "D:/TEST/P29/Topup/FunctionalRuns";    //Change for each participant
	var RunName = RunNameList[count-1];
	var InDicomPath    = InDicomList[count-1];


// Create FMR Project (TODO: comment parameters)
    var docFMR = bvqx.CreateProjectMosaicFMR("DICOM", InDicomPath, 
                                             NrVolPerRunList[count-1],  //nr of volumes
                                             4,          //nr of volumes to skip
                                             true,      //create AMR
                                             40,         //nr of slices
                                             RunName,    //STC prefix
                                             false,      //swap bytes
                                             448,448, //dimension of images in volume x, y
                                             2,          //nr of bytes per pixel, usually 2
                                             RunPath,    //saving directory
                                             1,          //number of volumes per file
                                             64,64    //dimension of image x, y
                                             );
							
 // Give prefixed names
    docFMR.SaveAs(RunName);


bvqx.PrintToLog(RunName + " finished.");

};

bvqx.PrintToLog("***/nThe_END/n***.");






