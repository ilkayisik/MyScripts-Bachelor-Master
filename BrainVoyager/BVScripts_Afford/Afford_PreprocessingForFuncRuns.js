// Batch script for BVQX
// Preprocessing for Functional Runs
// @author: Ayse Ilkay Isik for AFFORD project


var bvqx = BrainVoyagerQX;
bvqx.PrintToLog("---/n---");

//-----------------------------------------------------------------

// Enter the following values:
var MainDataPath = "D:/TEST/P29/Topup/FunctionalRuns";  //Change for each Subject
var NumberOfRuns = 5;
var SubjectRunPrefix = "P29_Run"   //Change for each Subject

// Enter fmr file paths //Change for each Subject

var InFmrList = ["D:/TEST/P29/Topup/FunctionalRuns/P29_Run1.fmr",
"D:/TEST/P29/Topup/FunctionalRuns/P29_Run2.fmr",
"D:/TEST/P29/Topup/FunctionalRuns/P29_Run3.fmr",
"D:/TEST/P29/Topup/FunctionalRuns/P29_Run4.fmr",
"D:/TEST/P29/Topup/FunctionalRuns/P29_Run5.fmr"
];

//----------------------------------------------------------------
for(count = 1; count <= NumberOfRuns; count++){
    
    var RunPath = MainDataPath
    var RunName = SubjectRunPrefix + String(count) + "_PP"; //
    var InFmrPath = InFmrList[count-1];
	
    // Open the run
    var docFMR = bvqx.OpenDocument(InFmrPath);

    // Slice scan time correction
    docFMR.CorrectSliceTiming(10, 2); // Scan order 10:Descending; Interpolation method  trilinear:0, cubic spline:1, windowed sinc:2
    ResultFileName = docFMR.FileNameOfPreprocessdFMR;
    docFMR.Close();

    docFMR = bvqx.OpenDocument(ResultFileName);
    
   
   // Motion Corection
    docFMR.CorrectMotionEx (240,       // target volume
						    2,         // interpolation method, 0 and 1:trilin./trilin., 2:trilin/sinc, 3:sinc/sinc
					          false,    //Use full data set (reduced data set is default in GUI)
						    100,      // Maximum number of iterations
					 	    true,     //Generate movies
						    true      //Generate extended log file	
   						    );


                                                 
    ResultFileName = docFMR.FileNameOfPreprocessdFMR;
    docFMR.Close();
	
	docFMR = bvqx.OpenDocument(ResultFileName);

 // High-pass filtering
    docFMR.TemporalHighPassFilterGLMFourier(2);
    ResultFileName = docFMR.FileNameOfPreprocessdFMR;
    docFMR.Close();

    docFMR = bvqx.OpenDocument(ResultFileName);

 // Give prefixed names
    docFMR.SaveAs(RunName);
    
    bvqx.PrintToLog(RunName + " finished.");
};

bvqx.PrintToLog("***/nThe_END/n***.");
