////
//  ZDSAppDelegate.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import "ZDSAppDelegate.h"
#import "PreferenceController.h"

#import "ZDSArticle.h"
#import "ZDSParserArticle.h"
@class ParserZSD;
@class ZDSUpdate;



@implementation ZDSAppDelegate
@synthesize article;

- (void)applicationDidFinishLaunching:(NSNotification *)aNotification
{
}

- (id) init{
    self = [super init];
    if (self){
        [[NSUserNotificationCenter defaultUserNotificationCenter] setDelegate:self];
        NSNotificationCenter *nc = [NSNotificationCenter defaultCenter];
        [nc addObserver:self selector:@selector(updateNotification:) name:ZDSUpdateNotification object:nil];
        _allNotification = [[NSMutableArray alloc] init];
        
    }
    return self;
}

- (void) awakeFromNib{
    //Création de la bar en haut
    statusItem = [[NSStatusBar systemStatusBar] statusItemWithLength:NSVariableStatusItemLength];
    [statusItem setMenu:statusMenu];
    [statusItem setHighlightMode:YES];
    [statusItem setImage:[NSImage imageNamed:@"icone_19.png"]];
          //Récupération des articles
    
   }




- (void)userNotificationCenter:(NSUserNotificationCenter *)center didActivateNotification:(NSUserNotification *)notification{
    for (NSMutableArray *array in _allNotification){
        if ([notification isEqualTo:[array objectAtIndex:4]]){
                       NSURL *url = [NSURL URLWithString:[array objectAtIndex:2]];
            [[NSWorkspace sharedWorkspace] openURL:url];
        }
    }
      
}

- (BOOL)userNotificationCenter:(NSUserNotificationCenter *)center shouldPresentNotification:(NSUserNotification *)notification{
    return YES;
}


-(void)updateNotification:(NSNotification *)note{
    NSMutableArray *arrayInfos = [[note userInfo] objectForKey:@"ZDSParser"];
    NSUserNotification *nn = [[NSUserNotification alloc] init];
    [nn setTitle:[arrayInfos objectAtIndex:0]];
    [nn setSubtitle:[NSString stringWithFormat:@"%@ vient de répondre.",[arrayInfos objectAtIndex:1]]];
   // NSImage *image = [[NSImage alloc] initWithContentsOfURL:[arrayInfos objectAtIndex:2] ];
    //[nn setContentImage:[arrayInfos objectAtIndex:4]];
    [nn setValue:@YES forKey:@"_showsButtons"];
    [nn setHasActionButton:YES];
    [nn setActionButtonTitle:@"Répondre"];
    [nn setOtherButtonTitle:@"Annuler"];
    [[NSUserNotificationCenter defaultUserNotificationCenter] deliverNotification:nn];
    [arrayInfos addObject:nn];
    [_allNotification addObject:arrayInfos];
    
     
}

@end
