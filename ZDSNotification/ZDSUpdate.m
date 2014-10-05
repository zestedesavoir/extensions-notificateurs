//
//  ZDSUpdate.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 05/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ZDSUpdate.h"
#import "PreferenceController.h"
@implementation ZDSUpdate

-(id)init{
    self = [super init];
    if (self){
    NSURL *url = [NSURL URLWithString:@"https://dl.dropboxusercontent.com/u/98367538/ClemNotificateur/update.txt"];
    NSString *message = [NSString stringWithContentsOfURL:url encoding: NSUTF8StringEncoding error:nil];
        NSArray *array = [message componentsSeparatedByString:@":"];
        version = [array objectAtIndex:0];
        raison = [array objectAtIndex:1];
        [self update];
    }
    return self;
}


-(void)update{
    NSString *nowVersion = [PreferenceController preferenceVersion];
    //NSLog(@"%hhd, versionNow: '%@', version de l'application : '%@'",[nowVersion isEqualToString:version],[PreferenceController preferenceVersion],version);
    if (![nowVersion isEqualToString:version]){
        
        NSString *raisonWindows = [[NSString alloc] initWithFormat:@"%@",raison];
        NSInteger choice = NSRunAlertPanel(@"Mise à jour :", raisonWindows,@"Faire la mise à jour." ,@"Utiliser comme même." ,nil );
        
        if (choice == NSAlertDefaultReturn){
            [[NSWorkspace sharedWorkspace]openURL:[NSURL URLWithString:@"https://zestedesavoir.com/forums/sujet/1154/clemnotificateur-pour-mac/"]];
            

        }
        
    }
   }
@end
