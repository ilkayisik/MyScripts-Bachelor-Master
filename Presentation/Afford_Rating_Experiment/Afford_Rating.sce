scenario = "Stimuli- Rating";
#randomize_trials = true;
default_font_size = 36;
default_background_color = 128,128,128;
active_buttons =10;
button_codes =1,2,3,4,5,6,7,8,9,10;
begin;


################################################# fixation cross
picture { 
 box {
    height = 10;
    width = 40;
    color = 255, 255, 255;}hor;
    x=0;y=0;
 box {
    height = 40;
    width = 10;
    color = 255, 255, 255;}ver;
    x=0;y=0;} fixcross;
#################################################


################################################# training trial
trial {
picture fixcross;
time=0;
duration=1000;

picture {box {height = 200; width = 200; color = 0, 0, 0;}training; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark in diesem Moment Ihr Verlangen nach einer Zigarette ist.\n\n
1     2     3     4     5     6     7     8     9\n
schwach                                    stark";}; x = 0; y = -300;};
code = "craving_training";
time=1000;
target_button=10;
duration=target_response;

picture {box training; x = 0; y = 100; bitmap {filename = "SAM_Valence.bmp";}; x=0; y=-300;};
code ="valence_training";
target_button=10;
duration=target_response;

picture {box training; x = 0; y = 100; bitmap {filename = "SAM_Arousal.bmp";}; x=0; y=-300;};
code ="arousal_training";
target_button=10;
duration=target_response;

picture {box training; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie vertraut Sie mit diesem Objekt sind.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "familiarity_training";
target_button=10;
duration=target_response; 

picture {box training; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark dieses Objekt motorische Assoziationen bei Ihnen hervorruft.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "association_training";
duration=target_response;
target_button=10; 
} training_trial;
#################################################


array {
###################################### control left
LOOP $k 5;
trial {
picture fixcross;
time=0;
duration=1000;

picture {bitmap {filename = "cl$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark in diesem Moment Ihr Verlangen nach einer Zigarette ist.\n\n
1     2     3     4     5     6     7     8     9\n
schwach                                    stark";}; x = 0; y = -300;};
code = "2$k";
time=1000;
target_button=10;
duration=target_response;

picture {bitmap {filename = "cl$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Valence.bmp";}; x=0; y=-300;};
code ="2$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "cl$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Arousal.bmp";}; x=0; y=-300;};
code ="2$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "cl$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie vertraut Sie mit diesem Objekt sind.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "2$k";
target_button=10;
duration=target_response; 

picture {bitmap {filename = "cl$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark dieses Objekt motorische Assoziationen bei Ihnen hervorruft.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "2$k";
duration=target_response;
target_button=10; 
} "trial_cl$k";
ENDLOOP;
######################################


###################################### control right
LOOP $k 5;
trial {
picture fixcross;
time=0;
duration=1000;

picture {bitmap {filename = "cr$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark in diesem Moment Ihr Verlangen nach einer Zigarette ist.\n\n
1     2     3     4     5     6     7     8     9\n
schwach                                    stark";}; x = 0; y = -300;};
code = "3$k";
time=1000;
target_button=10;
duration=target_response;

picture {bitmap {filename = "cr$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Valence.bmp";}; x=0; y=-300;};
code ="3$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "cr$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Arousal.bmp";}; x=0; y=-300;};
code ="3$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "cr$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie vertraut Sie mit diesem Objekt sind.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "3$k";
target_button=10;
duration=target_response; 

picture {bitmap {filename = "cr$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark dieses Objekt motorische Assoziationen bei Ihnen hervorruft.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "3$k";
duration=target_response;
target_button=10; 
} "trial_cr$k";
ENDLOOP;
######################################


###################################### smoke left
LOOP $k 5;
trial {
picture fixcross;
time=0;
duration=1000;

picture {bitmap {filename = "sl$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark in diesem Moment Ihr Verlangen nach einer Zigarette ist.\n\n
1     2     3     4     5     6     7     8     9\n
schwach                                    stark";}; x = 0; y = -300;};
code = "4$k";
time=1000;
target_button=10;
duration=target_response;

picture {bitmap {filename = "sl$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Valence.bmp";}; x=0; y=-300;};
code ="4$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "sl$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Arousal.bmp";}; x=0; y=-300;};
code ="4$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "sl$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie vertraut Sie mit diesem Objekt sind.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "4$k";
target_button=10;
duration=target_response; 

picture {bitmap {filename = "sl$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark dieses Objekt motorische Assoziationen bei Ihnen hervorruft.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "4$k";
duration=target_response;
target_button=10; 
} "trial_sl$k";
ENDLOOP;
######################################


###################################### smoke right
LOOP $k 5;
trial {
picture fixcross;
time=0;
duration=1000;

picture {bitmap {filename = "sr$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark in diesem Moment Ihr Verlangen nach einer Zigarette ist.\n\n
1     2     3     4     5     6     7     8     9\n
schwach                                    stark";}; x = 0; y = -300;};
code = "5$k";
time=1000;
target_button=10;
duration=target_response;

picture {bitmap {filename = "sr$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Valence.bmp";}; x=0; y=-300;};
code ="5$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "sr$k.bmp"; }; x = 0; y = 100; bitmap {filename = "SAM_Arousal.bmp";}; x=0; y=-300;};
code ="5$k";
target_button=10;
duration=target_response;

picture {bitmap {filename = "sr$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie vertraut Sie mit diesem Objekt sind.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "5$k";
target_button=10;
duration=target_response; 

picture {bitmap {filename = "sr$k.bmp"; }; x = 0; y = 100; text {font ="Arial"; font_size =23; caption = "Bitte geben Sie auf einer Skala von 1 bis 9 an,\nwie stark dieses Objekt motorische Assoziationen bei Ihnen hervorruft.\n\n
1     2     3     4     5     6     7     8     9\n
wenig                                         sehr";}; x = 0; y = -300;};
code = "5$k";
duration=target_response;
target_button=10; 
} "trial_sr$k";
ENDLOOP;
######################################

} alltrials;

begin_pcl;

alltrials.shuffle();

training_trial.present();

loop
	int j = 1
until
	j > alltrials.count()
begin
	alltrials[j].present();
	j=j+1
end;
