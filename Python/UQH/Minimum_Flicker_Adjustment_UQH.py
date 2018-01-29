#!/usr/bin/env pythoN
from psychopy import core, visual, event, filters, misc, monitors
import numpy as np
import copy
import colorsys

#create a window to draw in
mon = monitors.Monitor('UMRAM', width=38.5, distance=72.5)#fetch the most recent calib for this monitor

myWin = visual.Window(size =(1024, 768), fullscr = True, winType='pyglet', screen = 0, allowGUI = False, allowStencil = False, 
	monitor = 'UMRAM', color = 'gray', colorSpace='rgb')

#fixation dot

fixDot = visual.GratingStim(myWin, tex='none', mask='none', units='', pos=(0.0, 0.0), 
	size=0.005, sf=None, color='white', depth=1 )
fixDot.setAutoDraw(True) #draws for each frame

#constant variables
stimSize = 4
stimPosX = 5
stepSize = 0.002
waitSec = 3
temporalFreq = 18
loopDur = 1.0/temporalFreq #one loop (red gratings and grey gratings)
stimDur = loopDur/2

#for psychopy special RGB
def rgb_to_rgbPsychopy (inputRGB):
	return (inputRGB[0]*2-1), (inputRGB[1]*2-1), (inputRGB[2]*2-1)
						
def updateTex (hlsList):
	return np.array([[rgb_to_rgbPsychopy(colorsys.hls_to_rgb(hlsList[0], hlsList[1], hlsList[2])), [0.0 ,0.0 ,0.0]]])
	
def hlsCheck (value):
	if value < 0:
		value = 0
	if value > 1:
		value = 1
	return value
	
#determin initial colors as RGB
RE 	= [1.0 ,0.0 ,0.0] #red    R G B
GR 	= [0.0 ,1.0 ,0.0] #green  R G B
BL 	= [0.0 ,0.0 ,1.0] #blue   R G B
YE	= [1.0 ,1.0 ,0.0] #yellow R G B

#rgb to hls transformation
hlsRe = colorsys.rgb_to_hls(RE[0], RE[1], RE[2])
hlsGr = colorsys.rgb_to_hls(GR[0], GR[1], GR[2])
hlsBl = colorsys.rgb_to_hls(BL[0], BL[1], BL[2])
hlsYe = colorsys.rgb_to_hls(YE[0], YE[1], YE[2])

#making list for +/- when the buttons are pressed
hlsReList = [hlsRe[0],hlsRe[1],hlsRe[2]]
hlsGrList = [hlsGr[0],hlsGr[1],hlsGr[2]]
hlsBlList = [hlsBl[0],hlsBl[1],hlsBl[2]]
hlsYeList = [hlsYe[0],hlsYe[1],hlsYe[2]]

#hls to rgb transformation
rgbRe = colorsys.hls_to_rgb(hlsReList[0], hlsReList[1], hlsReList[2])
rgbGr = colorsys.hls_to_rgb(hlsGrList[0], hlsGrList[1], hlsGrList[2])
rgbBl = colorsys.hls_to_rgb(hlsBlList[0], hlsBlList[1], hlsBlList[2])
rgbYe = colorsys.hls_to_rgb(hlsYeList[0], hlsYeList[1], hlsYeList[2])

#fucking psychopy SPECIAL RGB transformation
S_rgbRe = rgb_to_rgbPsychopy(rgbRe) #S_ stands for SPECIAL
S_rgbGr = rgb_to_rgbPsychopy(rgbGr)
S_rgbBl = rgb_to_rgbPsychopy(rgbBl)
S_rgbYe = rgb_to_rgbPsychopy(rgbYe)

#grating texture
texRe = np.array([[S_rgbRe, [0.0 ,0.0 ,0.0]]])			   
texGr = np.array([[S_rgbGr, [0.0 ,0.0 ,0.0]]])		   
texBl = np.array([[S_rgbBl, [0.0 ,0.0 ,0.0]]])		   
texYe = np.array([[S_rgbYe, [0.0 ,0.0 ,0.0]]])
   
#save initial texture colors
texDefRe = np.array(copy.deepcopy(texRe))
texDefGr = np.array(copy.deepcopy(texGr))
texDefBl = np.array(copy.deepcopy(texBl))
texDefYe = np.array(copy.deepcopy(texYe))

#init variables
stimState = 0
prevState = -1

#create a clock
clock = core.Clock()
clock.reset()

while True:

	
	if stimState == 0:

		#texture setup!!!
		stimReL = visual.GratingStim(myWin, tex=texRe, units='deg', size=stimSize,  pos=(-stimPosX, 0.0), colorSpace='rgb')	#stimuli Red 	Left
		stimReR = visual.GratingStim(myWin, tex=texRe, units='deg', size=stimSize,  pos=( stimPosX, 0.0), colorSpace='rgb') #stimuli Red 	Right
		stim1 = stimReL
		stim2 = stimReR
		
	if stimState == 1:
		#texture setup!!!
		stimGrL = visual.GratingStim(myWin, tex=texGr, units='deg', size=stimSize,  pos=(-stimPosX, 0.0), colorSpace='rgb')	#stimuli Red 	Left
		stimGrR = visual.GratingStim(myWin, tex=texGr, units='deg', size=stimSize,  pos=( stimPosX, 0.0), colorSpace='rgb') #stimuli Red 	Right
		stim1 = stimGrL
		stim2 = stimGrR

	if stimState == 2:

		#texture setup!!!
		stimBlL = visual.GratingStim(myWin, tex=texBl, units='deg', size=stimSize,  pos=(-stimPosX, 0.0), colorSpace='rgb')	#stimuli Red 	Left
		stimBlR = visual.GratingStim(myWin, tex=texBl, units='deg', size=stimSize,  pos=( stimPosX, 0.0), colorSpace='rgb') #stimuli Red 	Right
		stim1 = stimBlL
		stim2 = stimBlR
		
	if stimState == 3:
		#texture setup!!!
		stimYeL = visual.GratingStim(myWin, tex=texYe, units='deg', size=stimSize,  pos=(-stimPosX, 0.0), colorSpace='rgb')	#stimuli Red 	Left
		stimYeR = visual.GratingStim(myWin, tex=texYe, units='deg', size=stimSize,  pos=( stimPosX, 0.0), colorSpace='rgb') #stimuli Red 	Right
		stim1 = stimYeL
		stim2 = stimYeR


	#01_accurate timer
	if clock.getTime()%loopDur < stimDur:
		state = 1
	if stimDur < clock.getTime()%loopDur < stimDur*2:
		state = 2

	#02_limit frame flips		
	if state != prevState:
		myWin.clearBuffer()
		if (state == 1):
			stim1.draw()
			stim2.draw()
			#print "A"
		if (state == 2):
			stim1.setPhase(0.5) #alter location of gray gratings
			stim2.setPhase(0.5) #alter location of gray gratings
			stim1.draw()
			stim2.draw()
			#print "B"
		myWin.flip()
		prevState = state
		
	#handle keypresses each frame
	for keys in event.getKeys(timeStamped=True):

		#change luminance
		if keys[0] in ['1']:
			
			if stimState == 0:
				hlsReList[1] += stepSize
				hlsReList[1] = hlsCheck(hlsReList[1])		
				texRe = updateTex (hlsReList)
				stim1 = stimReL
				stim2 = stimReR

			if stimState == 1:
				hlsGrList[1] += stepSize
				hlsGrList[1] = hlsCheck(hlsGrList[1])						
				texGr = updateTex (hlsGrList)
				stim1 = stimGrL
				stim2 = stimGrR

			if stimState == 2:
				hlsBlList[1] += stepSize
				hlsBlList[1] = hlsCheck(hlsBlList[1])		
				texBl = updateTex (hlsBlList)
				stim1 = stimBlL
				stim2 = stimBlR
				
			if stimState == 3:
				hlsYeList[1] += stepSize
				hlsYeList[1] = hlsCheck(hlsYeList[1])						
				texYe = updateTex (hlsYeList)
				stim1 = stimYeL
				stim2 = stimYeR

		if keys[0] in ['2']:
			
			if stimState == 0:
				hlsReList[1] -= stepSize
				hlsReList[1] = hlsCheck(hlsReList[1])		
				texRe = updateTex (hlsReList)
				stim1 = stimReL
				stim2 = stimReR
				
			if stimState == 1:
				hlsGrList[1] -= stepSize
				hlsGrList[1] = hlsCheck(hlsGrList[1])						
				texGr = updateTex (hlsGrList)
				stim1 = stimGrL
				stim2 = stimGrR
				
			if stimState == 2:
				hlsBlList[1] -= stepSize
				hlsBlList[1] = hlsCheck(hlsBlList[1])		
				texBl = updateTex (hlsBlList)
				stim1 = stimBlL
				stim2 = stimBlR
				
			if stimState == 3:
				hlsYeList[1] -= stepSize
				hlsYeList[1] = hlsCheck(hlsYeList[1])						
				texYe = updateTex (hlsYeList)
				stim1 = stimYeL
				stim2 = stimYeR

		if keys[0] in ['3']:
			
			if stimState == 0:
				texRe = copy.deepcopy(texDefRe)
				stim1 = stimReL
				stim2 = stimReR

			if stimState == 1:
				texGr = copy.deepcopy(texDefGr)
				stim1 = stimGrL
				stim2 = stimGrR
				
			if stimState == 2:
				texBl = copy.deepcopy(texDefBl)
				stim1 = stimBlL
				stim2 = stimBlR

			if stimState == 3:
				texYe = copy.deepcopy(texDefYe)
				stim1 = stimYeL
				stim2 = stimYeR

		if keys[0] in ['4']:
			stimState = (stimState + 1)%4
			myWin.clearBuffer()
			myWin.flip()
			core.wait(waitSec)
				
		#to quit
		if keys[0] in ['escape','q']:
						
			#hls to rgb transformation
			rgbRe = colorsys.hls_to_rgb(hlsReList[0], hlsReList[1], hlsReList[2])
			rgbGr = colorsys.hls_to_rgb(hlsGrList[0], hlsGrList[1], hlsGrList[2])
			rgbBl = colorsys.hls_to_rgb(hlsBlList[0], hlsBlList[1], hlsBlList[2])
			rgbYe = colorsys.hls_to_rgb(hlsYeList[0], hlsYeList[1], hlsYeList[2])

			#fucking psychopy SPECIAL RGB transformation
			S_rgbRe = rgb_to_rgbPsychopy(rgbRe) #S_ stands for psychopy's special RGB
			S_rgbGr = rgb_to_rgbPsychopy(rgbGr)
			S_rgbBl = rgb_to_rgbPsychopy(rgbBl)
			S_rgbYe = rgb_to_rgbPsychopy(rgbYe)

			#colors.pickle output
			equilumColors = {'S_equiRed':S_rgbRe, 'S_equiGreen':S_rgbGr, 'S_equiBlue':S_rgbBl, 'S_equiYellow':S_rgbYe, #S_ stands for psychopy's special RGB
				'equiRed':rgbRe, 'equiGreen':rgbGr, 'equiBlue':rgbBl, 'equiYellow':rgbYe}
			misc.toFile('equilumColors.pickle', equilumColors)
			print 'Adjusted colors saved as "equilumColors.pickle"'
			
			myWin.close()
			print int(clock.getTime())
			core.quit()
