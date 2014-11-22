//
//  ParserZSD.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HTMLNode.h"//;
#import "HTMLParser.h"//;

extern NSString * const ZDSUpdateNotification;
@interface ParserZSD : NSObject{
    NSMutableArray *_infosParserTopic; //Infos du parser pour les topics
    //1. Titre de l'article
    //2. La personne qui l'a répondu
    //3. Le lien
    //4. Le lien de l'image de l'auteur
    //5. L'id de la notification
    
    //Ou
    
    //1. Titre de la conversation
    //2. La personne qui l'a répondu
    //3. Le lien
    //4. Le lien de l'image de l'auteur
    
    NSMutableArray *article; //Liste des arcticle 
    
    
    
    
    
    }
@property (retain) NSTimer *timer;
@property (retain) HTMLParser *parser;
- (void)startTimer;
- (void)stopTimer;
- (void)reStartTimer;
- (void) parseZDS;
- (BOOL)findArrayInArray:(NSMutableArray *)arrayInArray Array:(NSMutableArray *)arrayValue;
-(void)updateNotificationWithArray: (NSMutableArray *)array;
- (NSString *) parseString:(NSString *)string; //Parse le string et en revoie un notre
- (NSString *)getPseudosZDS;
- (NSURL *)getUrlImagePseudos;
- (BOOL) isConnextionZDS;

@end
