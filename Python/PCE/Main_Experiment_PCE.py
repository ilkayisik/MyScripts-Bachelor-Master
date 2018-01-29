#!/usr/bin/env python
from psychopy import visual, core, event, gui, misc, monitors
import random, time
import numpy as np
#LAST TR=135
#Hit Q to quit during the experiment

#load last run of colorflash experiment
try:
    #try to load previous info
    config = misc.fromFile('config.pickle')
    print 'config.pickle is picked!'

#if loading fails use these values
except:
    #if no file use some defaults
    config = {'SubjectID':'Empty','SessionNr':'Empty','RunNr':'Empty',
        'stimSize':04.00,    #degrees of visual angle
        'stimPosX':08.50,    #degrees of visual angle
        'trialDur':20.00,    #seconds
        'restDur' :00.00,    #seconds !!!unused for this experiment
        'nrBlocks':4,        #number of blocks
        'nrBlockCycles':3,      #how many times the blocks will repeat        
        'flickFreq':4.0,      #hertz
        'letterDur':0.200,    #seconds
        'letterSize':0.85,    #degrees of visual angle
        'offsetDur':10}       #seconds
    print 'config.pickle is not picked! Using default configuration values.'

#set some configuration parametres that don't change
config['Date'] = time.strftime("%b_%d_%Y_%H:%M", time.localtime())
config['ExpVersion'] = 'DEMO_PCE_MAIN_EXPERIMENT'

#load colours determined in the last run of equiluminance experiment
try:
    
    equilumColors = misc.fromFile('equilumColors.pickle')
    print 'equilumColors.pickle is picked!'
    
#if no colors are loaded, use these values
except:

    equilumColors = {'S_equiRed'    :[ 1.0,-1.0,-1.0], 
                     'S_equiGreen'  :[-1.0, 1.0,-1.0], 
                     'S_equiBlue'   :[-1.0,-1.0, 1.0], 
                     'S_equiYellow' :[ 1.0, 1.0,-1.0]}    
    print 'equilumColors.pickle is not picked! Using default colors'

#create a user interface (DlgFromDict GUI)
configDlg = gui.DlgFromDict(dictionary = config, 
                            title = 'Please Check the Values', 

    #presentation order of the options
    order = ['ExpVersion', 'SubjectID', 'SessionNr', 'RunNr', 'Date', 
             'stimSize', 'stimPosX','trialDur', 'restDur', 'flickFreq',
             'letterDur', 'letterSize', 'offsetDur'], 
    
    #balloon tips
    tip   = {'SessionNr':'Session number.',
             'RunNr':'Write the run number.', 
             'stimSize'  :'degrees of visual angle',
             'stimPosX'  :'distance from a center of stimulus to the center of screen, in degrees of visual angle', 
             'trialDur'  :'seconds',
             'restDur'   :'seconds',
             'flickFreq' :'hertz',
             'letterDur' :'seconds',
             'letterSize':'degrees of visual angle',
             'offsetDur' :'second. initial delay after scanner signal'},
    
    #attributes can't be changed by the user
    fixed = ['ExpVersion','Date']
    )

#easier to use shorter variable names
trialDur  = config['trialDur']
condDur   = (trialDur)*config['nrBlocks'] #rest-R-rest-G-rest-B-rest-Y = 4, blocks are considered together with rests
offsetDur = config['offsetDur' ]
totalDur  = condDur*config['nrBlockCycles']                       #block cycle repetitions.
flipDur   = 1 / float(config['flickFreq'])  #hertz to second 
letterDur = config['letterDur']             #seconds

#stimuli positions (I should think sth else for this)
stimPos = [-config['stimPosX'], 0], [config['stimPosX'], 0]

#calculate 'number of measurements' to be entered in the MR operator's console
nrOfMeasurements = ((totalDur + offsetDur * 2) / 2)
print 'Number of Measurements =', nrOfMeasurements

#create log
logData = open(config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'log.txt', 'w')  #note: use 'a' to append

#set monitor information used in the experimental setup
MONITOR = monitors.Monitor('testMonitor', width=70, distance=130) #centimeters

#set screen (make 'fullscr = True' for fullscreen)
myWin = visual.Window(size =(1366, 768), fullscr = False, winType='pyglet', 
                      screen = 0, allowGUI = False, allowStencil = False,
                      monitor = MONITOR, color = 'black', colorSpace='rgb')
                      
#set the stimuli (I should reconsider this section with defs)
#!!!colors are not bound to config.pickle!!! (I should fix this)
#checkerboards
check1 = visual.GratingStim(win=myWin, tex=np.array([[1,-0.1],[-0.1,1]]), mask='none', size=12, pos=stimPos[0], sf=0.5, units='deg')
check2 = visual.GratingStim(win=myWin, tex=np.array([[1,-0.1],[-0.1,1]]), mask='none', size=12, pos=stimPos[1], sf=0.5, units='deg')

#squares on checkerboards
patch1 = visual.GratingStim(win=myWin, tex='none', mask='none', color=[0.8,0.8,0.8], size=8, pos=stimPos[0], sf=1, units='deg')
patch2 = visual.GratingStim(win=myWin, tex='none', mask='none', color=[0.8,0.8,0.8], size=8, pos=stimPos[1], sf=1, units='deg')

#colors on the checkerboards
filter1 = visual.GratingStim(win=myWin, tex='none', color=[0,0,-1], size=12, pos=stimPos[0], sf=1, opacity=0.6, units='deg')
filter2 = visual.GratingStim(win=myWin, tex='none', color=[-1,0,0], size=12, pos=stimPos[1], sf=1, opacity=0.6, units='deg')

#set the mirrored stimuli
#!!!colors are not bound to config.pickle!!! (I should fix this)
#checkerboards (mirrored)
check3 = visual.GratingStim(win=myWin, tex=np.array([[1,-0.1],[-0.1,1]]), mask='none', size=12, pos=stimPos[1], sf=0.5, units='deg')
check4 = visual.GratingStim(win=myWin, tex=np.array([[1,-0.1],[-0.1,1]]), mask='none', size=12, pos=stimPos[0], sf=0.5, units='deg')

#squares on the checkerboards
patch3 = visual.GratingStim(win=myWin, tex='none', mask='none', color=[0.8,0.8,0.8], size=8, pos=stimPos[1], sf=1, units='deg')
patch4 = visual.GratingStim(win=myWin, tex='none', mask='none', color=[0.8,0.8,0.8], size=8, pos=stimPos[0], sf=1, units='deg')

#colors on the checkerboards(mirrored)
filter3 = visual.GratingStim(win=myWin, tex='none', color=[0,0,-1], size=12, pos=stimPos[1], sf=1, opacity=0.6, units='deg')
filter4 = visual.GratingStim(win=myWin, tex='none', color=[-1,0,0], size=12, pos=stimPos[0], sf=1, opacity=0.6, units='deg')

#flickering gray circular patches
stable1 = visual.GratingStim(win=myWin, tex='none', mask='raisedCos', color=[-0.,-0.,-0.], size=7, pos=stimPos[0], interpolate=True, units='deg')
stable2 = visual.GratingStim(win=myWin, tex='none', mask='raisedCos', color=[-0.,-0.,-0.], size=7, pos=stimPos[1], interpolate=True, units='deg')
stable3 = visual.GratingStim(win=myWin, tex='none', mask='raisedCos', color=[-0.05,-0.05,-0.05], size=7, pos=stimPos[0], interpolate=True, units='deg')
stable4 = visual.GratingStim(win=myWin, tex='none', mask='raisedCos', color=[-0.05,-0.05,-0.05], size=7, pos=stimPos[1], interpolate=True, units='deg')

#fixation dot
fixDot = visual.GratingStim(myWin, tex='none', mask='none', units='deg',
                            pos=(0.0, 0.0), size=0.05, sf=None, color='white',
                            depth=1)
fixDot.setAutoDraw(True) #draws the fixation dot for each frame

#attention stimuli
attLetter = visual.TextStim(myWin,pos=(0.0, 0.0), units='deg', height=config['letterSize'], depth=-1, text='N/A')

#attention task randomization configurations
randomLetter = ['A', 'B', 'X', 'Y', '', ''] #empty characters are for jittering
attLetterColor = ['Yellow', 'Red']

#before the blank screen (I should more clearly comment this section)
prevState       = -2    #needed in while loop
prevAttState    = -1    #needed in while loop
attTargetState  = -1    #safeguard if subject presses a button before trigger
prevAttTargetState = attTargetState-1
logDataLine     = -1
logDataArray    = []

#create a clock
clock = core.Clock()
print 'Core clock has been resetted.' 
print int(clock.getTime())

#GUI 'OK', 'Cancel' buttons
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
        while clock.getTime() < totalDur + 20. and offsetState == 0: #+10 should be the last offset(FOR NOW)--FIX_ME--

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
                    
            if trialDur*0 <= clock.getTime()%condDur < trialDur*1:
                #BLOCK1
                state = 5

            elif trialDur*1 <= clock.getTime()%condDur < trialDur*2:
                #BLOCK2
                if clock.getTime()%(flipDur*2) < flipDur:
                    stable1.setOpacity(1), stable2.setOpacity(1)
                    stable3.setOpacity(0), stable4.setOpacity(0)
                    state = 1

                else:
                    stable1.setOpacity(0), stable2.setOpacity(0)
                    stable3.setOpacity(1), stable4.setOpacity(1)
                    state = 2

            elif trialDur*2 <= clock.getTime()%condDur < trialDur*3:
                #BLOCK3
                state = 6

            elif trialDur*3 <= clock.getTime()%condDur < trialDur*4:
                #BLOCK4
                if clock.getTime()%(flipDur*2) < flipDur:
                    stable1.setOpacity(1), stable2.setOpacity(1)
                    stable3.setOpacity(0), stable4.setOpacity(0)
                    state = 3

                else:
                    stable1.setOpacity(0), stable2.setOpacity(0)
                    stable3.setOpacity(1), stable4.setOpacity(1)
                    state = 4

            else:
                #resting state
                state = 0 #useless for this experiment
            
            #limit frame flips        
            if state != prevState or attState != prevAttState:
                myWin.clearBuffer()

                if (attState == 1):
                    attLetter.draw()
                    
                if (state==1 or state==2):
                    check1.draw(), check2.draw()
                    patch1.draw(), patch2.draw()
                    filter1.draw(), filter2.draw()
                    stable1.draw(), stable2.draw()
                    stable3.draw(), stable4.draw()
                    
                if (state==3 or state==4):
                    check3.draw(), check4.draw()
                    patch3.draw(), patch4.draw()
                    filter3.draw(), filter4.draw()
                    stable1.draw(), stable2.draw()
                    stable3.draw(), stable4.draw()
                    
                if (state==5):
                    check1.draw(), check2.draw()
                    patch1.draw(), patch2.draw()
                    filter1.draw(), filter2.draw()
                
                if (state==6):
                    check3.draw(), check4.draw()
                    patch3.draw(), patch4.draw()
                    filter3.draw(), filter4.draw()
                    
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

print float(clock.getTime())
#save configurations
misc.toFile(config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'config.txt', config)
print 'This run is completed, configurations saved as '+config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'config.txt'
#save attention task log
for i in range (0,logDataLine+1):
    logData.write('%s,%s,\n' %(logDataArray[i][0],logDataArray[i][1]))
print 'Log Data saved as '+config['SubjectID']+'_'+config['SessionNr']+'_'+config['RunNr']+'_'+'log.txt'

#cleanup
myWin.close()
core.quit()