//
//  AppController.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 28/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "AppController.h"
#import  "PreferenceController.h"

@implementation AppController

-(IBAction)showPreferencePanel:(id)sender{
    
    if(!preferenceController){
        preferenceController = [[PreferenceController alloc]init];
        
    }
    NSLog(@"Affichage des paramètres");
    [preferenceController showWindow:self];
}

@end
