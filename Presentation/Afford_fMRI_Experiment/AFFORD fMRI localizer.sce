scenario = "DFG_fMRI";
scenario_type = fMRI;
scan_period = 2500;
pulse_code = 20;
pulses_per_scan = 1;
no_logfile = false;
default_font_size = 36;
default_background_color = 128,128,128;
active_buttons = 5;
button_codes = 1,2,33,44,55;

response_logging = log_active;

# code 1 = left hand
# code 2 = right hand
# code 100 = trial L
# code 200 = trial R
# code 300 = trial C
# code 400 = trial S
# code 500 = fixation

begin;

#definition of default picture
picture {box {height = 5; width = 20; color = 255, 255, 255;}hor3; x=0;y=0; box { height = 20; width = 5; color = 255, 255, 255;}ver3; x=0;y=0;} default;

#definition of default fixation cross
picture {box {height = 5; width = 20; color = 255, 255, 255;}hor; x=0;y=0; box { height = 20; width = 5; color = 255, 255, 255;}ver; x=0;y=0;} fixcross;

#definition "control" pictures
array {
LOOP $c 35;
picture { bitmap { filename = "c$c.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0; box ver; x = 0; y = 0;};
ENDLOOP;
} controlstimuli;

#definition "smoking" pictures
array {
LOOP $d 35;
picture { bitmap { filename = "s$d.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0; box ver; x = 0; y = 0;};
ENDLOOP;
} smokingstimuli;


#definition of L/R for motor task
picture {
text {
caption = "L";
font_size = 100;
font = "Arial";
font_color = 255, 255, 255;
};
x = 0; y = 0;
}textL;

picture {
text {
caption = "R";
font_size = 100;
font = "Arial";
font_color = 255, 255, 255;
};
x = 0; y = 0;
}textR;

#definition "left hand" trial+fixation
trial {
trial_duration = 30000;              
trial_type = fixed;
picture fixcross;
time = 0;
duration = 500;
code = 100;
LOOP $a 10;
picture textL;
time = '500 + $a*1000 + $a*500';
duration = 1000;
target_button = 1;
ENDLOOP;
picture fixcross;
response_active = true;
time = 15000;
duration = 15000;
code = 500;
} left_hand;

#definition "right hand" trial
trial {
trial_duration = 30000;              
trial_type = fixed;
picture fixcross;
time = 0;
duration = 500;
code = 200;
LOOP $b 10;
picture textR;
time = '500 + $b*1000 + $b*500';
duration = 1000;
target_button = 2;
ENDLOOP;
picture fixcross;
response_active = true;
time = 15000;
duration = 15000;
code = 500;
} right_hand;



#definition visual trial
trial {
trial_duration = 30000;              
trial_type = fixed; 
stimulus_event {
picture fixcross;
response_active = true;
time=0;
duration=500;
code = 300;
} event_0;
stimulus_event {
picture fixcross;
response_active = true;
time= 500;
duration=1500;
} event_1; 
stimulus_event {
picture fixcross;
response_active = true;
time= 2500;
duration=1500;
} event_2;
stimulus_event {
picture fixcross;
response_active = true;
time= 4500;
duration=1500;
} event_3;
stimulus_event {
picture fixcross;
response_active = true;
time= 6500;
duration=1500;
} event_4;
stimulus_event {
picture fixcross;
response_active = true;
time= 8500;
duration=1500;
} event_5;
stimulus_event {
picture fixcross;
response_active = true;
time= 10500;
duration=1500;
} event_6;
stimulus_event {
picture fixcross;
response_active = true;
time= 12500;
duration=1500;
} event_7;
stimulus_event {
picture fixcross;
response_active = true;
time=15000;
duration=15000;
code = 500;
} event_8; 
} visual_trial;


trial{
trial_type = fixed;
	stimulus_event{
      picture fixcross;
      time = 0;
      } eventSteadyState;
} waiting_trial;





begin_pcl;

int k = 1;
int g = 1;

loop until (pulse_manager.main_pulse_count() == 1)
begin
end;

array <int> blocks [20] = {1,3,4,2,3,1,4,2,2,4,4,1,2,3,3,1,2,4,3,1};   #1 = LeftHand, 2 = RightHand 3 = ControlVisual 4 = SmokingVisual

eventSteadyState.set_duration (10000); # dummy in the beginning
waiting_trial.present();

loop
	int i = 1 
until
	i > 20
	begin
	if blocks[i] == 1
		then left_hand.present();
		i = i + 1;
	elseif blocks [i] == 2
		then right_hand.present();
		i = i + 1;
	elseif blocks [i] == 3
		then event_0.set_event_code ("300");
		event_1.set_stimulus(controlstimuli[k]);
		event_2.set_stimulus(controlstimuli[k+1]);
		event_3.set_stimulus(controlstimuli[k+2]);
		event_4.set_stimulus(controlstimuli[k+3]);
		event_5.set_stimulus(controlstimuli[k+4]);
		event_6.set_stimulus(controlstimuli[k+5]);
		event_7.set_stimulus(controlstimuli[k+6]);
		visual_trial.present();
		i = i + 1;	
		k = k + 7;
	elseif blocks [i] == 4
		then event_0.set_event_code ("400");
		event_1.set_stimulus(smokingstimuli[g]);
		event_2.set_stimulus(smokingstimuli[g+1]);
		event_3.set_stimulus(smokingstimuli[g+2]);
		event_4.set_stimulus(smokingstimuli[g+3]);
		event_5.set_stimulus(smokingstimuli[g+4]);
		event_6.set_stimulus(smokingstimuli[g+5]);
		event_7.set_stimulus(smokingstimuli[g+6]);
		visual_trial.present();
		i = i + 1;	
		g = g + 7;
	end;
end

