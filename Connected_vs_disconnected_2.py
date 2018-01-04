#to visualize the synchornicity between the repos 


import numpy as np
import scipy.stats as stats
import pylab as pl

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

#filelist = ['L_connected.txt', 'L_disconnected.txt','M_connected.txt', 'M_disconnected.txt','H_connected.txt', 'H_disconnected.txt']
#data=pd.concat([pd.read_csv(item, names=[item[:-4]]) for item in filelist], axis=1)
#ata.to_csv("data.csv",sep=',')



df = pd.read_csv("Connected_vs_disconnected.csv", names = [ 'L+M+H_Connected','L+M+H_Disconnected','M+H_Connected','M+H_Disconnected','H_Connected','H_Disconnected'])


# Create a list of the mean scores for each variable
mean_values = [df['L+M+H_Connected'].mean(),df['L+M+H_Disconnected'].mean(),df['M+H_Connected'].mean(),df['M+H_Disconnected'].mean(),df['H_Connected'].mean(),df['H_Disconnected'].mean()]
print(mean_values)


## mean_values2 = [df['Disconnected'].mean()]

# Create a list of variances, which are set at .25 above and below the score
variance = [df['L+M+H_Connected'].std(),df['L+M+H_Disconnected'].std(),df['M+H_Connected'].std(),df['M+H_Disconnected'].std(),df['H_Connected'].std(),df['H_Disconnected'].std()]
#variance2 = [df['Disconnected'].std()]
print (variance)


# Set the bar labels
bar_labels = ['H+M+L C','H+M+L D','M+H Conn','M+H Dis','High Conn','high Disc']

# Create the x position of the bars
x_pos = list(range(len(bar_labels)))


# Create the plot bars
# In x position
plt.bar(x_pos,
        # using the data from the mean_values
        mean_values,
        # with a y-error lines set at variance
        yerr=variance,
        # aligned in the center
        align='center',
        # with color
        color='#FFC222',
        # alpha 0.5
        alpha=0.5)

# add a grid
plt.grid()

# set height of the y-axis
max_y = max(zip(mean_values, variance)) # returns a tuple, here: (3, 5)
plt.ylim([0, (max_y[0] + max_y[1]) * 1.1])

# set axes labels and title
plt.ylabel('Score')
plt.xticks(x_pos, bar_labels)
plt.title('Mean Scores For Each Test and Error with STD')

plt.show()


plt.bar(x_pos,
        # using the data from the mean_values
        mean_values,
        # with a y-error lines set at variance
        yerr=variance,
        # aligned in the center
        align='center',
        # with color
        color='#FFC222',
        # alpha 0.5
        alpha=0.5)

# add a grid
plt.grid()

# set height of the y-axis

