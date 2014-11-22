//
//  ZDSParserArticle.m
//  ZDSNotification
//
//  Created by Odric Roux-Paris on 18/10/2014.
//  Copyright (c) 2014 Cirdo. All rights reserved.
//
#import "HTMLParser.h"
#import "ZDSParserArticle.h"
#import "ZDSArticle.h"
#import "ZDSAppDelegate.h"
#import "HTMLNode.h"

@implementation ZDSParserArticle

-(NSMutableArray *) parseArticle{
    //Parse la page html : https://zestedesavoir.com/articles/
    NSMutableArray *articleArray= [[NSMutableArray alloc] init];
    NSError *error = nil;
    HTMLParser *parser = [[HTMLParser alloc] initWithContentsOfURL:[NSURL URLWithString:@"https://zestedesavoir.com/articles/"] error:&error];
    
    HTMLNode *node = [parser body];
    NSArray *inputNode = [node findChildTags:@"article"];
    for (HTMLNode *input in inputNode){
       //On fait un seconde parsing
        
        
        HTMLNode *inputImage = [input findChildTag:@"img"];
        NSString *lienImage = [@"https://zestedesavoir.com" stringByAppendingString:[inputImage getAttributeNamed:@"src"]];
        NSLog(@"%@",lienImage);
        NSImage *imageArticle = [[NSImage alloc] initByReferencingURL:[NSURL URLWithString:lienImage]];
        
                               
        
        //Parsing du lien de l'article et du nom de l'article
       HTMLNode *nodeH3 = [input findChildTag:@"h3"];
        NSLog(@"%@",[self parseString:[[[nodeH3 findChildTags:@"a"] objectAtIndex:0] rawContents]]);
        
        
               NSString *lien = [@"https://zestedesavoir.com" stringByAppendingString:[[[nodeH3 findChildTags:@"a"] objectAtIndex:0] getAttributeNamed:@"href"]];
        
        
        
        ZDSArticle *article = [[ZDSArticle alloc] initWithNomArticle:[self parseString:[[[nodeH3 findChildTags:@"a"] objectAtIndex:0] rawContents]] Url:[NSURL URLWithString:lien] Image:imageArticle];
        [articleArray addObject:article];
        
        if ([articleArray count] > 10){
            break;
        }
    }
    
    return articleArray;
}
- (NSString *) parseString:(NSString *)string{
    BOOL save = NO;
    
    NSMutableString *stringResult = [[NSMutableString alloc] init];
    for (int i = 0; i < [string length]; i++){
        if (save){
            [stringResult appendFormat:@"%c",[string characterAtIndex:i]];
        }
        if ([string characterAtIndex:i] == '>'){
            save = YES;
            
            
        } else if ([string characterAtIndex:i] == '<'){
            save = NO;
        }
        
    }
    
    return[stringResult  stringByReplacingCharactersInRange:NSMakeRange([stringResult length]-1, 1) withString:@""];
    
}

@end
