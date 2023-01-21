import React from 'react';
import { useNavigate, createSearchParams, useSearchParams } from 'react-router-dom';
import { Form, Button } from 'react-bootstrap'
import { ArrowLeftCircleFill, ArrowRightCircleFill, PlusCircleFill, Dot } from 'react-bootstrap-icons';
import './CookBooks.css'
import './CookBooksList.css'
import Header from '../components/Header'

function CookBooksList () {
    const navigate = useNavigate();
    const token = localStorage.token
    console.log(token)
    
    function landing () {
        navigate('/')
    }

    /* Interface for cookbook details */
    interface CookBookInterface {
        id: string;
        name: string;
        creator: string;
        recipe_ids: string;
        creator_username: string;
    }
    
    // Get cookbook_id
    const [searchParams, setSearchParams] = useSearchParams();
    const recipeId = Number(searchParams.get("id"));

    let cookbook_ids: number[];
    // Gets cookbook data from backend
    const [cookbooksReturned, setCookbooksReturned] = React.useState<CookBookInterface[]>([]);
    async function getCookBookData () {
        if (token) {
            fetch('http://localhost:8080/users/info', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
            })
            })
            .then(r => {
                if (r.status === 200) {
                r.json().then(data => {
                    cookbook_ids = data.users[0].cookbook_ids.map(Number);
                    fetch('http://localhost:8080/cookbooks/info', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            'Accept' : 'application/json'
                        },
                        body: JSON.stringify({
                            cookbook_ids: cookbook_ids
                        })
                    })
                    .then(r => {
                        return r.json();
                    })
                    .then(rJson => {
                        setCookbooksReturned(rJson.cookbooks);
                    })
                })
                } else {
                    r.json().then(data => {
                        alert('cb fetch failed');
                        return false;
                    })
                }
            })
            
        }
    }

    /* Update cookbook data whenever page refreshes */ 
    React.useEffect(()=>{
        getCookBookData()
    },[])




    function makeCookBook (cb : CookBookInterface) {
        return (
            <div className='cblbox'>
                <h2 className='cookbooksSingleHeader' onClick={event => handleClickCookBook(cb.id, cb.recipe_ids, cb.name)}>
                        {cb.name}
                </h2>
                <span className='PlusCircleFill'><PlusCircleFill></PlusCircleFill></span>
                
            </div>
        )
    }

    /* Make list of card components */
    let cookbooks : any = [];
    if (cookbooksReturned) {
        cookbooks = cookbooksReturned.map(cb => makeCookBook(cb))
    } 

    function handleClickCookBook (id : String, r_ids : any, name : String) {
        r_ids.push(recipeId)
        fetch('http://localhost:8080/cookbooks/edit', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                token: token,
                cookbook: {
                    id: id,
                    name: name,
                    recipe_ids: r_ids
                }
            })
        }).then(r => {
            if (r.status===200) {
                return r.json()
            }
        }).then(rJson => {
            if(rJson.success) {
                alert('Successfully added recipe!')
                navigate({
                    pathname: "/cookbook",
                    search: createSearchParams({
                        id: '' + id
                    }).toString()
                });
            } else alert('Could not add recipe.')
            
        })
        
    }
      return <>
      <Header/>
      <br></br>
      <div className='cookbooksHeader'>
        <h1 className='cookbookTitle'> Add to Cookbook </h1>
      </div>
      <p></p>
        <div id='cook_container' className='cblContainer'>
                {cookbooks}            
        </div>
        
      </>;
    }
    
    export default CookBooksList;