# -*- coding: utf-8 -*-
"""
Created on Wed Feb  5 13:42:58 2014

Load and save all subjects as h5 file

"""

# lazy import
from mvpa2.suite import *

# enter input paths
DATA_PATH = os.path.join('/home/faruk/HYPERALIGNMENT_TEST/FMRI_DATA')
ATTR_PATH = os.path.join('/home/faruk/HYPERALIGNMENT_TEST/ATTRIBUTES')
MASK_PATH = os.path.join('/home/faruk/HYPERALIGNMENT_TEST/MASKS')

# enter the file names
AII_DATA    = 'AII_UQH_RUN01-05.nii.gz'
MKAPTAN_DATA= 'MKAPTAN_UQH_RUN01-05.nii.gz'
MK_DATA     = 'MK_UQH_RUN01-05.nii.gz'
ZP_DATA     = 'ZP_UQH_RUN01-05.nii.gz'

AII_ATTR    = 'COL_MVPA_UQH_ATTRIBUTES_3discard.txt'
MKAPTAN_ATTR= 'COL_MVPA_UQH_ATTRIBUTES_3discard.txt'
MK_ATTR     = 'COL_MVPA_UQH_ATTRIBUTES_3discard.txt'
ZP_ATTR     = 'COL_MVPA_UQH_ATTRIBUTES_3discard.txt'

AII_MASK    = 'AII_CLUSTER_MASK_P0.05_Z5.nii.gz'
MKAPTAN_MASK= 'MKAPTAN_CLUSTER_MASK_P0.05_Z5.nii.gz'
MK_MASK     = 'MK_CLUSTER_MASK_P0.05_Z5.nii.gz'
ZP_MASK     = 'ZP_CLUSTER_MASK_P0.05_Z5.nii.gz'

# source of class targets and chunks definitions
AII_ATTR    = SampleAttributes(os.path.join(ATTR_PATH, AII_ATTR))
MKAPTAN_ATTR= SampleAttributes(os.path.join(ATTR_PATH, MKAPTAN_ATTR))
MK_ATTR     = SampleAttributes(os.path.join(ATTR_PATH, MK_ATTR))
ZP_ATTR     = SampleAttributes(os.path.join(ATTR_PATH, ZP_ATTR))

# load data
ds_AII     = fmri_dataset(samples = os.path.join(DATA_PATH, AII_DATA),
                          targets = AII_ATTR.targets,
                          chunks  = AII_ATTR.chunks,
                          mask    = os.path.join(MASK_PATH, AII_MASK)
                          )
                          
ds_MKAPTAN = fmri_dataset(samples = os.path.join(DATA_PATH, MKAPTAN_DATA),
                          targets = MKAPTAN_ATTR.targets,
                          chunks  = MKAPTAN_ATTR.chunks,
                          mask    = os.path.join(MASK_PATH, MKAPTAN_MASK)
                          )
                          
ds_MK      = fmri_dataset(samples = os.path.join(DATA_PATH, MK_DATA),
                          targets = MK_ATTR.targets,
                          chunks  = MK_ATTR.chunks,
                          mask    = os.path.join(MASK_PATH, MK_MASK)
                          )
                          
ds_ZP      = fmri_dataset(samples = os.path.join(DATA_PATH, ZP_DATA),
                          targets = ZP_ATTR.targets,
                          chunks  = ZP_ATTR.chunks,
                          mask    = os.path.join(MASK_PATH, ZP_MASK)
                          )                          

# preprocess
poly_detrend(ds_AII, polyord=1, chunks_attr='chunks')
zscore(ds_AII, param_est=('targets', ['rest'])) 

poly_detrend(ds_MKAPTAN, polyord=1, chunks_attr='chunks')
zscore(ds_MKAPTAN, param_est=('targets', ['rest']))

poly_detrend(ds_MK, polyord=1, chunks_attr='chunks')
zscore(ds_MK, param_est=('targets', ['rest']))

poly_detrend(ds_ZP, polyord=1, chunks_attr='chunks')
zscore(ds_ZP, param_est=('targets', ['rest']))

# delete the unwanted data parts using the names from the attribute files
ds_AII     = ds_AII[ds_AII.sa.targets != 'rest'   ]
ds_AII     = ds_AII[ds_AII.sa.targets != 'discard']

ds_MKAPTAN = ds_MKAPTAN[ds_MKAPTAN.sa.targets != 'rest'   ]
ds_MKAPTAN = ds_MKAPTAN[ds_MKAPTAN.sa.targets != 'discard']

ds_MK      = ds_MK[ds_MK.sa.targets != 'rest'   ]
ds_MK      = ds_MK[ds_MK.sa.targets != 'discard']

ds_ZP      = ds_ZP[ds_ZP.sa.targets != 'rest'   ]
ds_ZP      = ds_ZP[ds_ZP.sa.targets != 'discard']

# save
h5save('/home/faruk/HYPERALIGNMENT_TEST/AII_MKAPTAN_MK_ZP_LOCROI_PREPROCESSED.h5', 
       [ds_AII, ds_MKAPTAN, ds_MK, ds_ZP]
       )

## load
#ds1, ds2 = h5load('/tmp/out.h5'


















             
                          