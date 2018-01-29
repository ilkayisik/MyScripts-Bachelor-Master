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

# code 1 = left button
# code 2 = right button
# code 9 = fixation cross
# code 10... = CLOLH
# code 20... = CLORH
# code 30... = CROLH
# code 40... = CRORH
# code 50... = SLOLH
# code 60... = SLORH
# code 70... = SROLH
# code 80... = SRORH

begin;

#definition of default picture
picture {box {height = 10; width = 40; color = 255, 255, 255;}hor3; x=0;y=0; box { height = 40; width = 10; color = 255, 255, 255;}ver3; x=0;y=0;} default;

#definition of default fixation cross and default cross
picture {box {height = 10; width = 40; color = 255, 255, 255;}hor; x=0;y=0; box { height = 40; width = 10; color = 255, 255, 255;}ver; x=0;y=0;} fixcross;


#definition of colored fixation crosses for task
box {height = 10; width = 40; color = 127, 0, 255;}hor1; #lila
box {height = 40; width = 10; color = 127, 0, 255;}ver1;
box {height = 10; width = 40; color = 140, 70, 0;}hor2; #braun
box {height = 40; width = 10; color = 140, 70, 0;}ver2;


array {
#definition "control left orientation left hand" trial
LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed;
all_responses = false; 
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; box hor1; x = 0; y = 0;box ver1; x = 0; y = 0;}; #blue
time=1850;
duration=180;
target_button = 1;
code = "10$k";
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial1_$k";
ENDLOOP;



#definition "control left orientation right hand" trial

LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; box hor2; x = 0; y = 0;box ver2; x = 0; y = 0;}; #brown
time=1850;
duration=180;
target_button = 2;
code = "20$k";
picture { bitmap { filename = "cl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial2_$k";
ENDLOOP;

#definition "control right orientation left hand" trial

LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; box hor1; x = 0; y = 0;box ver1; x = 0; y = 0;}; #blue
time=1850;
duration=180;
target_button = 1;
code = "30$k";
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial3_$k";
ENDLOOP;
#definition "control right orientation right hand" trial

LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; box hor2; x = 0; y = 0;box ver2; x = 0; y = 0;}; #brown
time=1850;
duration=180;
target_button = 2;
code = "40$k";
picture { bitmap { filename = "cr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial4_$k";
ENDLOOP;


#definition "smoke left orientation left hand" trial

LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; box hor1; x = 0; y = 0;box ver1; x = 0; y = 0;}; #blue
time=1850;
duration=180;
target_button = 1;
code = "50$k";
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial5_$k";
ENDLOOP;
#definition "smoke left orientation right hand" trial

LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; box hor2; x = 0; y = 0;box ver2; x = 0; y = 0;}; #brown
time=1850;
duration=180;
target_button = 2;
code = "60$k";
picture { bitmap { filename = "sl$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial6_$k";
ENDLOOP;

#definition "smoke right orientation left hand" trial
LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; box hor1; x = 0; y = 0;box ver1; x = 0; y = 0;}; #blue
time=1850;
duration=180;
target_button = 1;
code = "70$k";
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial7_$k";
ENDLOOP;


#definition "smoke right orientation right hand" trial

LOOP $k 5;
trial {
trial_duration = 5000;              
trial_type = fixed; 
all_responses = false;
picture fixcross;
time=0;
duration=1000;
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; }; #default
time=1000;
duration=700;
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=1700;
duration=150;
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; box hor2; x = 0; y = 0;box ver2; x = 0; y = 0;}; #brown
time=1850;
duration=180;
target_button = 2;
code = "80$k";
picture { bitmap { filename = "sr$k.bmp"; }; x = 0; y = 0; box hor; x = 0; y = 0;box ver; x = 0; y = 0;}; #white
time=2030;
duration=1970;
} "trial8_$k";
ENDLOOP;
} alltrials;

trial{
	stimulus_event{
      picture fixcross;
      time = 0;
      }event1;
}waiting_trial;

begin_pcl;

int mrSteadyStateDuration = 10000;
int repetitionCount = 2;

loop until (pulse_manager.main_pulse_count() == 1)
begin
end;

array <int> orders[122] = {3,2,0,2,4,0,3,8,1,3,0,1,2,0,5,3,0,3,7,5,5,0,7,3,7,4,6,0,2,0,1,0,2,0,2,1,0,3,4,0,1,0,6,0,7,0,7,0,6,8,0,5,0,7,0,6,0,5,5,4,0,4,4,0,8,6,0,5,0,4,0,8,0,7,0,1,7,0,5,1,0,2,8,7,1,0,3,0,4,0,4,0,2,0,6,7,6,3,0,6,8,2,6,0,1,0,8,5,0,5,0,8,0,4,1,8,8,0,3,6,2,0};
array <int> durations[122] = {5000,5000,5000,5000,5000,2500,5000,5000,5000,5000,2500,5000,5000,5000,5000,5000,2500,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,5000,2500,5000,2500,5000,2500,5000,5000,5000,5000,5000,10000,5000,17500,5000,5000,5000,2500,5000,2500,5000,5000,5000,5000,5000,5000,5000,5000,7500,5000,5000,5000,7500,5000,5000,2500,5000,5000,7500,5000,7500,5000,2500,5000,2500,5000,12500,5000,5000,10000,5000,5000,5000,5000,5000,5000,5000,2500,5000,2500,5000,5000,5000,2500,5000,2500,5000,5000,5000,5000,5000,5000,5000,5000,5000,2500,5000,2500,5000,5000,2500,5000,2500,5000,2500,5000,5000,5000,5000,2500,5000,5000,5000,7500};


array <int> ii[8][repetitionCount*5];
array <int> pp[8];


array <int> lll_indices [orders.count()];

array <int> stimuli [orders.count()];

loop
	int jj=1
until
	jj>8
begin
	array <int> k[repetitionCount*5]; # k is the array which will fill in ii with different stimuli indices in it
	loop int a=1 until a>repetitionCount*5 begin
		k[a] = 1+((a-1)/repetitionCount);
		a=a+1;
	end;
	pp[jj] = 1; # pp =[1,1,1,1,1,1,1,1] will be like this in the end
	k.shuffle();#shuffle k so that the stim for each trial will be in different order
	ii[jj] = k; # fill ii array with shuffled k arrays
	jj=jj+1 # increment j 
end;

event1.set_duration (mrSteadyStateDuration); # for dummy images in the beginning
waiting_trial.present(); 

loop                            # main loop to choose stimuli and present
	int j = 1
until
	j > orders.count()           # goes until the number of the items in the orders array
begin
	if (orders[j] == 0) then # NULL  #if indice is showing 0,  
		event1.set_duration (durations[j]); #then change its duration based on the duration array
		waiting_trial.present(); #and present waiting trial, this is for ISI
	else
		int lll = ii[orders[j]][pp[orders[j]]]; #go to the main array containing stimulus for each trial and chose one integer to present stimulus
		pp[orders[j]] = pp[orders[j]]+1; # this incrementation lets you to go to a different stimulus each time you go to chose the stimuli for a trial
		lll_indices[j] = lll;
		stimuli[j] = lll;
		alltrials[orders[j]*5+lll-5].present(); #goes to alltrials array and chose stimuli to present
	end;
	
	j=j+1
end;


# writing in separate files: orders, durations, ii, pp, lll,
###########################                                
output_file orders_Run3 = new output_file;
orders_Run3.open( "orders_Run3.txt" ); 

loop
	int i = 1
until
	i > 122
begin
	orders_Run3.print(orders[i]);
	orders_Run3.print( "\n" );  
	i = i + 1
end;
orders_Run3.close();
###########################

###########################
output_file durations_Run3 = new output_file;
durations_Run3.open( "durations_Run3.txt" ); 

loop
	int i = 1
until
	i > 122
begin
	durations_Run3.print(durations[i]);
	durations_Run3.print( "\n" );  
	i = i + 1
end;
durations_Run3.close();
###########################

###########################
output_file ii_Run3 = new output_file;
ii_Run3.open( "ii_Run3.txt" ); 

loop
	int i = 1
until
	i > 10
begin
	ii_Run3.print(ii[1][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[2][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[3][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[4][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[5][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[6][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[7][i]);
	ii_Run3.print( "\t" );  
	ii_Run3.print(ii[8][i]);
	ii_Run3.print( "\n" );
	i = i + 1
end;
ii_Run3.close();
###########################


###########################
output_file pp_Run3 = new output_file;
pp_Run3.open( "pp_Run3.txt" ); 

loop
	int c = 1
until
	c > 8
begin
	pp_Run3.print(pp[c]);
	pp_Run3.print( "\t" );  
	c = c + 1
end;
pp_Run3.close();
###########################


###########################
output_file lll_Run3 = new output_file;
lll_Run3.open( "lll_Run3.txt" ); 

loop
	int d = 1
until
	d > lll_indices.count()
begin
	lll_Run3.print(lll_indices[d]);
	lll_Run3.print( "\n" );  
	d = d + 1
end;
lll_Run3.close();
###########################


###########################
int cond1 = 1;
int cond2 = 2;
int cond3 = 3;
int cond4 = 4;
int cond5 = 5;
int cond6 = 6;
int cond7 = 7;
int cond8 = 8;

output_file exp_Run3 = new output_file;
exp_Run3.open( "exp_Run3.txt" ); 

loop
	int i = 1
until
	i > orders.count()
begin
	exp_Run3.print(orders[i]);
	exp_Run3.print( "\t" );
		
	if orders[i] == 0 then
		exp_Run3.print(0);
		exp_Run3.print("\n");
		i = i + 1;
	elseif orders[i] != 0 then
		exp_Run3.print(stimuli[i]);
		exp_Run3.print( "\n" );
		i = i + 1;
	end;
end;

exp_Run3.close();

###########################