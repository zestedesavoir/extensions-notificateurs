//
//  ParserZSD.h
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 26/08/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HTMLNode.h";
#import "HTMLParser.h";


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

- (NSMutableArray *) parseZDS;


- (NSString *) parseString:(NSString *)string; //Parse le string et en revoie un notre

@end
