# This is to find all the events belonging to particular repo/tag/branch and allign them together is a csv as well as a text file.

import pandas as pd
from time import mktime
from datetime import date
import datetime
import time
results= pd.DataFrame(columns=['branch','time', 'repo', 'tag'])
df = pd.read_csv("EventsSeq.csv",delimiter=',')

new =datetime.datetime(2015,1,4,17,10,43).timestamp()
print (new)
a=[]

for i in df.iloc[:, 2].unique():
    name = []

    for row in range(0, len(df)):
        if (df.iloc[row, 2] == i): #and df.iloc[row, 0] == "branch"):       #if(df.iloc[row,5]=='branch'):
            if(df.iloc[row, 5] == "branch"):
                name.append(df.iloc[row,4])
               #print(row, name)
            if pd.isnull(df.iloc[row, 4]):
                split = str(df.iloc[row, 6]).split('/')
            elif df.iloc[row,4]!="":
             split = str(df.iloc[row,4]).split('/')


            for j in split:
                if j in name:
                    a.append(str(i)+"_"+str(df.iloc[row,0])+"_"+str(j))
                    #a.append(str(df.iloc[row,0]))
                else:
                    continue
        else:
            continue

    a.append("")
results = pd.DataFrame({"branch":a})
results.to_csv("results.csv", sep=',',index=False)
with open("results.txt.txt", "w") as text_file:
    text_file.write(str(a))















"""total_create=0
total_delete=0
total_push=0
total_pullreq=0
total_pullrew=0
wcd=0.0
wcpush=0.0
wcc=0.0
wcpull=0.0
wcpullrew=0.0
wdc=0.0
wdd=0.0
wdpush=0.0
wdpull=0.0
wdpullrew=0.0
wpp=0.0
wpc=0.0
wpd=0.0
wppull=0.0
wppullewq=0.0
#wplc,wpld,wplpl,wplpush,wplplrew=0.0
#wprc,wprd,wprpush,wprpr,wprpull=0.0

for i in range(0,len(results)):
    split = str(results.iloc[i,0]).split('_')

    if(split[1] == 'CreateEvent'):
           total_create=total_create+1
    elif split[1] == 'DeleteEvent':
            total_delete= total_delete+1
    elif split[1] == 'PushEvent':
           total_push+=1
    elif split[1]=='PullRequestEvent':
            total_pullreq+=1
    elif split[1] == 'PullRequestReviewCommentEvent':
            total_pullrew+=1
for i in range(1, len(results)):

    if(results.iloc[i,0]=='CreateEvent'):
        split = str(results.iloc[i, 0]).split('_')
    ref=split[2]
    repo=split[0]
    for i in range(1, len(results)):
     if(ref,repo in str(results.iloc[i, 0]).split('_')):

     #while(split[1]!= "DeleteEvent"):

          if results.iloc[i,0]=='CreateEvent' and results.iloc[i-1,0]=='CreateEvent' :
             wcc+=1
          elif (results.iloc[i,0]=='DeleteEvent' and results.iloc[i-1,0]=='CreateEvent') :
            wcd+=1
          elif (results.iloc[i, 0] == 'PushEvent' and results.iloc[i - 1, 0] == 'CreateEvent'):
             wcpush+=1
          elif (results.iloc[i,0]=='PullRequestEvent' and results.iloc[i-1,0]=='CreateEvent') :
                 wcpull+=1
          elif (results.iloc[i,0]=='PullRequestReviewCommentEvent' and results.iloc[i-1,0]=='CreateEvent') :
             wdpullrew+=1

print("delete to create",wdc/total_delete)
print("create to create",wcc/total_create,"create to del",wcd/total_create,"create to pullrew",wcpullrew/total_create,"create to push",wcpush/total_create,"Create to pull",wcpull/total_create)

"""
"""def build_chain(results, chain={}):

        #index = 1
    for index in range(1,len(results)):
        for word in results.iloc[index,0]:
            key = results.iloc[index - 1,0]
            if key in chain:
                chain[key].append(word)
            else:
                chain[key] = [word]
            #index += 1

        return chain"""

