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

begin;

#definition of default picture
picture {box {height = 10; width = 40; color = 255, 255, 255;}hor3; x=0;y=0; box { height = 40; width = 10; color = 255, 255, 255;}ver3; x=0;y=0;} default;

#definition of default fixation cross
picture {box {height = 10; width = 40; color = 255, 255, 255;}hor; x=0;y=0; box { height = 40; width = 10; color = 255, 255, 255;}ver; x=0;y=0;} fixcross;


trial{
trial_type = fixed;
	stimulus_event{
      picture fixcross;
      time = 0;
		duration = 610000;
      }event1;
}waiting_trial;

begin_pcl;


#loop until (pulse_manager.main_pulse_count() == 1)
#begin
#end;

waiting_trial.present(); 














