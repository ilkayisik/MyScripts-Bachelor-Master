#!/usr/bin/env python
from psychopy import visual, core, event, data, logging, gui, hardware, misc
from random   import randint
import math, random, time, scipy, os, csv
import numpy as np
#LAST TR=106
#load last run of colorflash experiment
try:
	#try to load previous info
	config = misc.fromFile('ROILOC_config.pickle')
	print 'ROILOC_config.pickle is picked!'
#if loading fails use these values
except:
	#if no file use some defaults
	config = {'Observer':'Faruk','SubjectID':'SUBJ01','SessionNr':'00','RunNr':'00','Gender':['Male','Female'],
		'attTask':True,
		'stimSize':03.50,	#degrees of visual angle
		'stimPosX':05.00,	#degrees of visual angle
		'trialDur':12.00,	#second
		'restDur' :12.00,	#second
		'flickFreq':16.0,	#hertz
		'letterDur':0.200,	#second
		'letterSize':0.6,	#degrees of visual angle
		'offsetDur':10}		#second
	print 'ROILOC_config.pickle is not picked! Using default configuration values.'

#set some configuration parametres that don't change
config['Date'] = time.strftime("%b_%d_%Y_%H:%M", time.localtime())
config['ExpVersion'] = '1.4.0'

#create a DlgFromDict GUI
configDlg = gui.DlgFromDict(dictionary=config, title='ROI Localizer Experiment', 
    order = ['ExpVersion','Observer','SubjectID','SessionNr','RunNr','Gender','Date',
		'attTask','stimSize','stimPosX','trialDur','restDur','flickFreq','letterDur','letterSize','offsetDur'], 
    tip   = {'SubjectID':'Please fill in with CAPITAL letters and a number, eg:"SUBJECT01".','SessionNr':'Session number.','RunNr':'Write the run number.',
		'attTask'	:'has no use for now',
		'stimSize'	:'degrees of visual angle',
		'stimPosX'	:'distance from a center of stimulus to the center of screen, in degrees of visual angle',
		'trialDur'	:'seconds',
		'restDur'	:'seconds',
		'flickFreq'	:'hertz',
		'letterDur'	:'seconds',
		'letterSize':'degrees of visual angle',
		'offsetDur'	:'second. initial delay after scanner signal'},
    fixed = ['ExpVersion','Date'] #this attribute can't be changed by the user
    )

#configurations (safeguard to override GUI values)
stimSize = config['stimSize']			#degrees of visual angle
stimPosX = config['stimPosX']			#degrees of visual angle
trialDur  = config['trialDur']
restDur   = config['restDur' ]			#may include jitter later
condDur   = (trialDur+restDur)			#name can later be changed as 4 stroke cycle
offsetDur = config['offsetDur' ]
totalDur  = condDur*8					#Number is the number of repetitions.
flipDur   = 1 / float(config['flickFreq'])		#hertz to second 
letterDur = config['letterDur']			#second
letterSize= config['letterSize']		#degrees of visual angle

TR=((totalDur+offsetDur*2)/2)
print "TR="; print TR;

#log creation for behavioral data
logData = open(config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'ROI_LOC_log.txt', 'w')	#'a' is to append

#set screen
myWin = visual.Window(size =(1024, 768), fullscr = True, winType='pyglet', screen = 0, allowGUI = False, allowStencil = False,
    monitor = 'UMRAM', color = 'gray', colorSpace='rgb')

#messeges if required
message = visual.TextStim(myWin,pos=(0.0,-0.9),text='Hit Q to quit')
message.setAutoDraw(False) #draws for each frame

#create texture array to use for checkerboards
checkTexture1 =  np.array([
							[ -1,  1],
							[  1, -1]
							])

#create negative texture array
checkTexture2 = -1 * checkTexture1

#setup checkerboard stimuli
rectLP = visual.GratingStim(myWin, tex=checkTexture1, units='deg', size=stimSize,  pos=(-stimPosX, 0.0), phase=(0.25,0.25)) #left positive
rectLN = visual.GratingStim(myWin, tex=checkTexture2, units='deg', size=stimSize,  pos=(-stimPosX, 0.0), phase=(0.25,0.25)) #left negative
rectRP = visual.GratingStim(myWin, tex=checkTexture1, units='deg', size=stimSize,  pos=( stimPosX, 0.0), phase=(0.25,0.25)) #right positive
rectRN = visual.GratingStim(myWin, tex=checkTexture2, units='deg', size=stimSize,  pos=( stimPosX, 0.0), phase=(0.25,0.25)) #right negative

#fixation dot
fixDot = visual.GratingStim(myWin, tex='none', mask='none', units='', pos=(0.0, 0.0), 
	size=0.005, sf=None, color='white', depth=1 )
fixDot.setAutoDraw(True) #draws for each frame

#attention stimuli
attLetter = visual.TextStim(myWin,pos=(0.0, 0.0), units='deg', height=letterSize, depth=-1, text='N/A')

#attention task randomization configurations
randomLetter = ['A', 'B', 'X', 'Y', '', '']
attLetterColor = ['Yellow', 'Red']

#before the blank screen
prevState = -2		#needed in while loop
prevAttState = -1	#needed in while loop
attTargetState = -1	#safeguard if the subject presses any button before the scanner signal
prevAttTargetState = attTargetState-1	#safeguard
logDataLine = -1
logDataArray = []

#create a clock
clock = core.Clock()
print 'Core clock has been resetted.' 
print int(clock.getTime())

#GUI OK Cancel
if configDlg.OK: #this will be True (user hit OK) or False (cancelled)
	
	misc.toFile('ROILOC_config.pickle',config)
	print config['Date']
	print 'Configurations saved as "ROILOC_config.pickle"'
	print 'Waiting for the scanner signal...'
	
	#wait for the scanner signal to start
	scannerStartSignal = True
	while scannerStartSignal:
		for keys in event.getKeys():
			if keys in ['6']: #UMRAM scanner sends "6".
				scannerStartSignal = False
	
	print int(clock.getTime())
	clock.reset()#whenever you like
	print int(clock.getTime())
	print 'The experiment has begun!'

	#offset loop not efficient coding!!!
	while clock.getTime() < offsetDur:
		#offset duration
		offsetState = 1
		state = -1
		myWin.flip()
		
	else:
		offsetState = 0
		print int(clock.getTime())
		print 'Offset time is up!'
		clock.reset()#whenever you like
		
		#render loop		
		while clock.getTime() < totalDur + offsetDur and offsetState == 0: #+10 offsetDur is last offset FOR NOW

			if clock.getTime()%condDur*1 < condDur: #will always be true. Safeguard for some later issue		
				
				if clock.getTime()%(letterDur*4) < letterDur*1: #related to attention task
					attState = 1
					
					if (prevAttState != attState):
						whichRandomLetter = randomLetter[random.randint(0,len(randomLetter)-1)] 
						whichLetterColor  = attLetterColor[random.randint(0,len(attLetterColor)-1)]
						attLetter.setText(whichRandomLetter)
						attLetter.setColor(whichLetterColor)
						#determine attention target state (duration of the target state is letterDur*4 for now, look above)
						if whichRandomLetter == 'X' and whichLetterColor == 'Red':
							prevAttTargetState = 0 #inside the if to prevent getting response for ''(empty) letters used for jitter
							attTargetState = 1
							logDataLine = logDataLine + 1
							logDataArray.append(['Target','NoResp'])
							
						elif whichRandomLetter == 'X' and whichLetterColor == 'Yellow':
							prevAttTargetState = 0 #inside the if to prevent getting response for ''(empty) letters used for jitter
							attTargetState = 3
							logDataLine = logDataLine + 1
							logDataArray.append(['Target','NoResp'])
									
						elif whichRandomLetter !='':
							prevAttTargetState = 0 #inside the if to prevent getting response for ''(empty) letters used for jitter
							attTargetState = 2
							logDataLine = logDataLine + 1
							logDataArray.append(['NoTarget','NoResp'])

				elif clock.getTime()%(letterDur*4) >= letterDur*1:
					attState = 0
					
			if trialDur*1 <= clock.getTime()%condDur < trialDur*2:
				
				#red-green color patch states
				if clock.getTime()%(flipDur*2) < flipDur:
					whichRectL = rectLP
					whichRectR = rectRP
					state = 1
					
				else:
					whichRectL = rectLN
					whichRectR = rectRN
					state = 2

			elif trialDur*3 <= clock.getTime()%condDur < trialDur*4:

				#blue-yellow color patch states
				if clock.getTime()%(flipDur*2) < flipDur:
					whichRectL = rectLN
					whichRectR = rectRN
					state = 3

				else:
					whichRectL = rectLP
					whichRectR = rectRP
					state = 4

			else:
				#resting state
				state = 0
			
			#limit frame flips		
			if state != prevState or attState != prevAttState:
				myWin.clearBuffer()
				if (attState == 1):
					attLetter.draw()
				if (state>0):
					whichRectL.draw()
					whichRectR.draw()
				myWin.flip()
				
			#handle keypresses each frame
			for keys in event.getKeys(timeStamped=True):
				#handling attention task control
				if attTargetState != prevAttTargetState:							
					#attention control depending on attTargetState value
					if attTargetState == 1 and keys[0] in ['1']:
						print 'Hit'
						prevAttTargetState = attTargetState
						logDataArray[logDataLine][1] = 'Hit'
						
					if attTargetState == 3 and keys[0] in ['4']:
						print 'Hit'
						prevAttTargetState = attTargetState
						logDataArray[logDataLine][1] = 'Hit'
						
						
					if attTargetState == 2 and keys[0] in ['1','4']:
						print 'False Alarm'
						prevAttTargetState = attTargetState
						logDataArray[logDataLine][1] = 'FA'

					if attTargetState == 1 and keys[0] in ['4']:
						print 'Opposite Key'
						prevAttTargetState = attTargetState
						logDataArray[logDataLine][1] = 'OppKey'
					
					if attTargetState == 3 and keys[0] in ['1']:
						print 'Opposite Key'
						prevAttTargetState = attTargetState
						logDataArray[logDataLine][1] = 'OppKey'
				
					if attTargetState in range (1,4) and keys [0] in ['2','3']: #in range is here to check attTargetStage
						print 'Invalid Response'
						prevAttTargetState = attTargetState
						logDataArray[logDataLine][1] = 'InvResp'
				
				#to quit
				if keys[0] in ['escape','q']:
					myWin.close()
					print int(clock.getTime())
					#save attention task log
					for i in range (0,logDataLine+1):
						logData.write('%s,%s,\n' %(logDataArray[i][0],logDataArray[i][1]))
					logData.write('%s,' %(int(clock.getTime())))
					core.quit()
		
			prevState = state		
			prevAttState = attState		
			
else: 
	print 'User cancelled.'



print int(clock.getTime())
#save configurations
misc.toFile(config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'ROI_LOC_config.txt', config)
print 'This run is completed, configurations saved as '+config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'ROI_LOC_config.txt'
#save attention task log
for i in range (0,logDataLine+1):
	logData.write('%s,%s,\n' %(logDataArray[i][0],logDataArray[i][1]))
print 'Log Data saved as '+config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'ROI_LOC_log.txt'
