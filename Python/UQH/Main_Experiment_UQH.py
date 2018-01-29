#!/usr/bin/env python GO TO 79. LINE TO CHANGE THE BACKGROUND COLOR
from psychopy import visual, core, event, data, logging, gui, hardware, misc
from random   import randint
import math, random, time, scipy, os, csv
import numpy as np
#LAST TR=154
#load last run of colorflash experiment
try:
	#try to load previous info
	config = misc.fromFile('config.pickle')
	print 'config.pickle is picked!'

#if loading fails use these values
except:
	#if no file use some defaults
	config = {'Observer':'Faruk','SubjectID':'SUBJ01','SessionNr':'00','RunNr':'00','Gender':['Male','Female'],
		'attTask':True,
		'stimSize':04.00,	#degrees of visual angle
		'stimPosX':05.00,	#degrees of visual angle
		'trialDur':12.00,	#second
		'restDur' :12.00,	#second
		'flickFreq':16.0,	#hertz
		'letterDur':0.200,	#second
		'letterSize':0.6,	#degrees of visual angle
		'offsetDur':10}		#second
	print 'config.pickle is not picked! Using default configuration values.'
	
#load last run of equiluminance
try:
	#try to load previous info
	equilumColors = misc.fromFile('equilumColors.pickle')
	print 'equilumColors.pickle is picked!'
	
#if loading colors fails, use these values
except:
	#if no file use some defaults
	equilumColors = {'S_equiRed':[1.0,-1.0,-1.0], 'S_equiGreen':[-1.0, 1.0,-1.0], 'S_equiBlue':[-1.0,-1.0,1.0], 'S_equiYellow':[1.0, 1.0,-1.0]} #use default RGB values	
	print 'equilumColors.pickle is not picked! Using default colors'

#set some configuration parametres that don't change
config['Date'] = time.strftime("%b_%d_%Y_%H:%M", time.localtime())
config['ExpVersion'] = '3.0.0'

#create a DlgFromDict GUI
configDlg = gui.DlgFromDict(dictionary=config, title='Colorflash Experiment', 
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
stimSize  = config['stimSize']			#degrees of visual angle
stimPosX  = config['stimPosX']			#degrees of visual angle
trialDur  = config['trialDur']
restDur   = config['restDur' ]			#may include jitter later
condDur   = (trialDur+restDur)*4		#rest-R-rest-G-rest-B-rest-Y
offsetDur = config['offsetDur' ]
totalDur  = condDur*3					#Number is the number of 4 stroke cycle repetitions.
flipDur   = 1 / float(config['flickFreq'])		#hertz to second 
letterDur = config['letterDur']			#second
letterSize= config['letterSize']		#degrees of visual angle

TR=((totalDur+offsetDur*2)/2)
print "TR="; print TR;

#log creation for behavioral data
logData = open(config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'log.txt', 'w')	#'a' is to append

#set screen
myWin = visual.Window(size =(1024, 768), fullscr = True, winType='pyglet', screen = 0, allowGUI = False, allowStencil = False,
    monitor = 'testMonitor', color = 'gray', colorSpace='rgb')

#messeges if required
message = visual.TextStim(myWin,pos=(0.0,-0.9),text='Hit Q to quit')
message.setAutoDraw(False) #draws for each frame

#rect stimulus creation
texRGr = np.array([[equilumColors['S_equiRed'   ],	[0,0,0]]])
texGrR = np.array([[[0,0,0],	equilumColors['S_equiRed']]])

texGGr = np.array([[equilumColors['S_equiGreen' ],	[0,0,0]]])
texGrG = np.array([[[0,0,0],	equilumColors['S_equiGreen']]])

texBGr = np.array([[equilumColors['S_equiBlue'  ],	[0,0,0]]])
texGrB = np.array([[[0,0,0],	equilumColors['S_equiBlue']]])

texYGr = np.array([[equilumColors['S_equiYellow'],	[0,0,0]]])
texGrY = np.array([[[0,0,0],	equilumColors['S_equiYellow']]])

rectLR1 = visual.GratingStim(myWin, tex=texRGr, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  red-green grating
rectLR2 = visual.GratingStim(myWin, tex=texGrR, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  red-green grating

rectLG1 = visual.GratingStim(myWin, tex=texGGr, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  green-red grating
rectLG2 = visual.GratingStim(myWin, tex=texGrG, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  green-red grating
 
rectLB1 = visual.GratingStim(myWin, tex=texBGr, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#right red-green grating
rectLB2 = visual.GratingStim(myWin, tex=texGrB, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#right green-red grating

rectLY1 = visual.GratingStim(myWin, tex=texYGr, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  blue-yellow grating
rectLY2 = visual.GratingStim(myWin, tex=texGrY, mask='none', units='deg', pos=(-stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  yellow-blue grating

rectRR1 = visual.GratingStim(myWin, tex=texRGr, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  red-green grating
rectRR2 = visual.GratingStim(myWin, tex=texGrR, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  red-green grating

rectRG1 = visual.GratingStim(myWin, tex=texGGr, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  green-red grating
rectRG2 = visual.GratingStim(myWin, tex=texGrG, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  green-red grating
 
rectRB1 = visual.GratingStim(myWin, tex=texBGr, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#right red-green grating
rectRB2 = visual.GratingStim(myWin, tex=texGrB, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#right green-red grating

rectRY1 = visual.GratingStim(myWin, tex=texYGr, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  blue-yellow grating
rectRY2 = visual.GratingStim(myWin, tex=texGrY, mask='none', units='deg', pos=( stimPosX, 0.0),
 size=stimSize, sf=None, colorSpace='rgb')	#left  yellow-blue grating



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
	
	misc.toFile('config.pickle',config)
	print config['Date']
	print 'Configurations saved as "config.pickle"'
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
		clock.reset()#whenever you like
		
		#render loop		
		while clock.getTime() < totalDur + offsetDur and offsetState == 0: #+10 should be the last offset(FOR NOW)--FIX_ME--

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
				
				#red-gray color patch states
				if clock.getTime()%(flipDur*2) < flipDur:
					whichRectL = rectLR1
					whichRectR = rectRR1
					state = 1
					
				else:
					whichRectL = rectLR2
					whichRectR = rectRR2
					state = 2

			elif trialDur*3 <= clock.getTime()%condDur < trialDur*4:

				#green-gray color patch states
				if clock.getTime()%(flipDur*2) < flipDur:
					whichRectL = rectLG1
					whichRectR = rectRG1
					state = 3

				else:
					whichRectL = rectLG2
					whichRectR = rectRG2
					state = 4

			elif trialDur*5 <= clock.getTime()%condDur < trialDur*6:

				#blue-gray color patch states
				if clock.getTime()%(flipDur*2) < flipDur:
					whichRectL = rectLB1
					whichRectR = rectRB1
					state = 5

				else:
					whichRectL = rectLB2
					whichRectR = rectRB2
					state = 6

			elif trialDur*7 <= clock.getTime()%condDur < trialDur*8:

				#yellow-gray color patch states
				if clock.getTime()%(flipDur*2) < flipDur:
					whichRectL = rectLY1
					whichRectR = rectRY1
					state = 7

				else:
					whichRectL = rectLY2
					whichRectR = rectRY2
					state = 8


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
misc.toFile(config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'config.txt', config)
print 'This run is completed, configurations saved as '+config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'config.txt'
#save attention task log
for i in range (0,logDataLine+1):
	logData.write('%s,%s,\n' %(logDataArray[i][0],logDataArray[i][1]))
print 'Log Data saved as '+config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'log.txt'
